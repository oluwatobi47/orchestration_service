package uk.ac.bcu.invorchestrator.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Expression;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.support.DefaultMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.ac.bcu.invorchestrator.dto.DataResponse;
import uk.ac.bcu.invorchestrator.dto.InternalRoute;
import uk.ac.bcu.invorchestrator.dto.order.OrderStatus;
import uk.ac.bcu.invorchestrator.dto.warehouse.ProductValidationRequest;
import uk.ac.bcu.invorchestrator.service.IOrderService;
import uk.ac.bcu.invorchestrator.util.ProcessValidationException;
import uk.ac.bcu.invorchestrator.util.RouteRegistrationUtil;
import uk.ac.bcu.orderservice.proxy.Order;
import uk.ac.bcu.orderservice.proxy.OrderItems;
import uk.ac.bcu.orderservice.proxy.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderService orderServiceApi;
    private final ExceptionHandler exceptionHandler;
    private final ObjectMapper mapper;

    public OrderServiceImpl(OrderService orderServiceApi, ExceptionHandler exceptionHandler, ObjectMapper mapper) {
        this.orderServiceApi = orderServiceApi;
        this.exceptionHandler = exceptionHandler;
        this.mapper = mapper;
    }

    @Override
    public void handlePlaceOrder(RouteDefinition routeDefinition) {
        routeDefinition
                .setExchangePattern(ExchangePattern.InOut)
                .process(exchange -> {
                    var body = exchange.getIn().getBody();
                    var orderRequest = mapper.convertValue(body, Order.class);
                    exchange.setProperty("orderRequest", orderRequest);
                    exchange.setProperty("clientId", orderRequest.getClientId());
                    var message = new DefaultMessage(exchange);
                    message.setBody(orderRequest.getOrderItems(), List.class);
                    exchange.setIn(message);
                })
                .to("direct:validate-order-availability")
                .to("direct:initiate-order")
                .end();
    }

    private void initiateOrder(RouteDefinition routeDefinition) {
        routeDefinition
                .setExchangePattern(ExchangePattern.InOut)
                .process(exchange -> {
                    var request = exchange.getProperty("orderRequest", Order.class);
                    var response = orderServiceApi.placeOrder(request);
                    var dataResponse = new DataResponse(null, true, response);
                    var message = new DefaultMessage(exchange);
                    message.setBody(dataResponse);
                    exchange.setMessage(message);
                })
                .onException(Exception.class)
                .log("An exception occurred: ${exception.message}")
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    private void validateOrderAvailability(RouteDefinition routeDefinition) {
        routeDefinition
                .setExchangePattern(ExchangePattern.InOut)
                .split(new Expression() {
                    @Override
                    public <T> T evaluate(Exchange exchange, Class<T> type) {
                        List<OrderItems> items =  exchange.getIn().getBody(List.class);
                        var clientId = exchange.getProperty("clientId", String.class);
                        return (T) items.stream()
                                .map(orderItem ->
                                        new ProductValidationRequest(clientId, orderItem.getProductId(), orderItem.getReqQuantity()))
                                .collect(Collectors.toList());
                    }
                })
                .parallelProcessing()
                .stopOnException() // Would return validation or exception message if any of the products are not available
                .to("direct:validate-product")
                .end();
    }

    private void rollBackOrderItemCount(RouteDefinition routeDefinition) {
        routeDefinition
                .setExchangePattern(ExchangePattern.InOut)
                .split(new Expression() {
                    @Override
                    public <T> T evaluate(Exchange exchange, Class<T> type) {
                        var order =  exchange.getProperty("orderData", Order.class);
                        return (T) order.getOrderItems().stream()
                                .map(orderItem ->
                                        new ProductValidationRequest(order.getClientId(), orderItem.getProductId(), orderItem.getReqQuantity()))
                                .collect(Collectors.toList());
                    }
                })
                .parallelProcessing()
                .stopOnException(true) // Would return response or exception message if update request fails
                .to("direct:increment-product")
                .end();
    }

    @Override
    public void registerAdditionalHandlers(Function<String, RouteDefinition> function) {
        List<InternalRoute> internalOrderProcesses = new ArrayList<>();

        // Internal route to initiate an order
        internalOrderProcesses.add(new InternalRoute(this::initiateOrder, "direct:initiate-order"));

        // Internal route to validate multiple order items
        internalOrderProcesses.add(new InternalRoute(this::validateOrderAvailability, "direct:validate-order-availability"));

        // Internal route to update multiple order item state in the warehouse
        internalOrderProcesses.add(new InternalRoute(this::rollBackOrderItemCount, "direct:update-product-state-in-warehouse"));

        // Register new processes as routes in the camel context
        RouteRegistrationUtil.registerRouteHandlers(internalOrderProcesses, function);
    }

    @Override
    public void handleCancelOrder(RouteDefinition routeDefinition) {
        routeDefinition
                .setExchangePattern(ExchangePattern.InOut)
                .process(exchange -> {
                    var orderRef = exchange.getMessage().getHeader("orderRef", String.class);
                    var orderData = orderServiceApi.getOrderByOrderRef(orderRef);
                    var order = orderData.get(0);

                    // End process execution if order is not in pending state
                    if (!order.getOrderStatus().equals(OrderStatus.PROCESSING.toString())) {
                        throw new ProcessValidationException(HttpStatus.BAD_REQUEST, "Order is currently being processed or is already cancelled");
                    }
                    orderServiceApi.updateOrderStatus(orderRef, OrderStatus.CANCELLED.toString());
                    exchange.setProperty("orderData", order);
                })
                .to("direct:update-product-state-in-warehouse")
                .process(exchange -> {
                    var dataResponse = new DataResponse(null, true, "Order cancelled successfully");
                    var message = new DefaultMessage(exchange);
                    message.setBody(dataResponse);
                    exchange.setMessage(message);
                })
//                .marshal().json(JsonLibrary.Jackson)
                .onException(Exception.class)
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    @Override
    public void handleGetOrders(RouteDefinition routeDefinition) {
        routeDefinition
                .setExchangePattern(ExchangePattern.InOut)
                .process(exchange -> {
                    var clientId = exchange.getMessage().getHeader("clientId", String.class);
                    var orders = orderServiceApi.getOrdersByClientId(clientId);
                    var dataResponse = new DataResponse(orders);
                    var message = new DefaultMessage(exchange);
                    message.setBody(dataResponse);
                    exchange.setMessage(message);
                })
                .onException(Exception.class)
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

}
