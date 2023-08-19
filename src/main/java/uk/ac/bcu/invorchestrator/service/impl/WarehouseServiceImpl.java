package uk.ac.bcu.invorchestrator.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.http.HttpMethods;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.support.DefaultMessage;
import org.springframework.stereotype.Service;
import uk.ac.bcu.invorchestrator.config.ServiceEndpoints;
import uk.ac.bcu.invorchestrator.dto.DataResponse;
import uk.ac.bcu.invorchestrator.dto.InternalRoute;
import uk.ac.bcu.invorchestrator.dto.warehouse.ProductDTO;
import uk.ac.bcu.invorchestrator.dto.warehouse.ProductValidationRequest;
import uk.ac.bcu.invorchestrator.dto.warehouse.WarehouseResponse;
import uk.ac.bcu.invorchestrator.service.ResponseTransformer;
import uk.ac.bcu.invorchestrator.util.RouteRegistrationUtil;
import uk.ac.bcu.invorchestrator.service.WarehouseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final ServiceEndpoints serviceEndpoint;
    private final ExceptionHandler exceptionHandler;
    private final ResponseTransformer<WarehouseResponse> responseTransformer;
    private final ObjectMapper mapper;

    public WarehouseServiceImpl(ServiceEndpoints serviceEndpoint, ExceptionHandler exceptionHandler, ResponseTransformer<WarehouseResponse> responseTransformer, ObjectMapper mapper) {
        this.serviceEndpoint = serviceEndpoint;
        this.exceptionHandler = exceptionHandler;
        this.responseTransformer = responseTransformer;
        this.mapper = mapper;
    }

    private void registerProductValidationRoute(RouteDefinition route) {
        route
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.GET)
                .setHeader(Exchange.HTTP_QUERY, new SimpleExpression("number=${body.quantity}"))
                .toD(String.format("%s/${body.clientId}/products/${body.productId}/availability?bridgeEndpoint=true", serviceEndpoint.getWarehouseServiceUrl()))
                .to("log:DEBUG?showBody=true&showHeaders=true")
                .onException(Exception.class)
                .log("An exception occurred: ${exception.message}")
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    private void incrementProductRoute(RouteDefinition route) {
        route
                .setExchangePattern(ExchangePattern.InOut)
                // Transform request to external api payload
                .process(exchange -> {
                    var request = exchange.getIn().getBody(ProductValidationRequest.class);
                    exchange.setProperty("clientId", request.getClientId());
                    exchange.setProperty("productId", request.getProductId());
                    var body = new HashMap<String, Object>();
                    body.put("quantity", request.getQuantity());
                    var message = new DefaultMessage(exchange);
                    message.setBody(body);
                    exchange.setMessage(message);
                })
                .marshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.PUT)
                .toD(String.format("%s/${exchangeProperty.clientId}/products/${exchangeProperty.productId}/quantity?bridgeEndpoint=true", serviceEndpoint.getWarehouseServiceUrl()))
                .to("log:DEBUG?showBody=true&showHeaders=true")
                .unmarshal().json(JsonLibrary.Jackson)
                .onException(Exception.class)
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    @Override
    public void handleGetProduct(RouteDefinition process) {
        process
                .marshal().json(JsonLibrary.Jackson)
                .toD(String.format("%s/${header.clientId}/products/${header.productId}?bridgeEndpoint=true", serviceEndpoint.getWarehouseServiceUrl()))
                .unmarshal().json(JsonLibrary.Jackson)
                .process(exchange -> responseTransformer.transformToDataResponseMessage(exchange, ProductDTO.class))
                .onException(Exception.class)
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    @Override
    public void handleGetAllClientProducts(RouteDefinition process) {
        process
                .marshal().json(JsonLibrary.Jackson)
                .toD(String.format("%s/${header.clientId}/products?bridgeEndpoint=true", serviceEndpoint.getWarehouseServiceUrl()))
                .unmarshal().json(JsonLibrary.Jackson)
                .process(exchange -> {
                    var responseBody = mapper.convertValue(exchange.getMessage().getBody(), WarehouseResponse.class);
                    var map = mapper.convertValue(responseBody.getData(), new TypeReference<Map<String, Object>>() {});
                    var response = new DataResponse<List<ProductDTO>>(null);
                    if(map.containsKey("products_quantity")) {
                        var productList = mapper.convertValue(map.get("products_quantity"), new TypeReference<List<ProductDTO>>() {});
                        response.setData(productList);
                    }
                    var message = new DefaultMessage(exchange);
                    message.setBody(response);
                    exchange.setMessage(message);
                })
                .onException(Exception.class)
                .log("An exception occurred: ${exception.message}")
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    @Override
    public void handleAddProduct(RouteDefinition process) {
        process
                .marshal().json(JsonLibrary.Jackson)
                .toD(String.format("%s/${header.clientId}/products?bridgeEndpoint=true", serviceEndpoint.getWarehouseServiceUrl()))
                .unmarshal().json(JsonLibrary.Jackson)
                .process(exchange -> responseTransformer.transformToDataResponseMessage(exchange, ProductDTO.class))
                .onException(Exception.class)
                .log("An exception occurred: ${exception.message}")
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    @Override
    public void handleUpdateProduct(RouteDefinition process) {
        process
                .marshal().json(JsonLibrary.Jackson)
                .toD(String.format("%s/${header.clientId}/products/${header.productId}?bridgeEndpoint=true", serviceEndpoint.getWarehouseServiceUrl()))
                .unmarshal().json(JsonLibrary.Jackson)
                .process(exchange -> responseTransformer.transformToDataResponseMessage(exchange, ProductDTO.class))
                .onException(Exception.class)
                .log("An exception occurred: ${exception.message}")
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    @Override
    public void handleDeleteProduct(RouteDefinition process) {
        process
                .marshal().json(JsonLibrary.Jackson)
                .toD(String.format("%s/${header.clientId}/products/${header.productId}?bridgeEndpoint=true", serviceEndpoint.getWarehouseServiceUrl()))
                .unmarshal().json(JsonLibrary.Jackson)
                .process(exchange -> responseTransformer.transformToDataResponseMessage(exchange, Boolean.class))
                .onException(Exception.class)
                .log("An exception occurred: ${exception.message}")
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    @Override
    public void registerAdditionalHandlers(Function<String, RouteDefinition> routeDefinitionFunction) {
        List<InternalRoute> utilityProcesses = new ArrayList<>();

        // Internal route to validate availability of a product
        utilityProcesses.add(new InternalRoute(this::registerProductValidationRoute, "direct:validate-product"));
        utilityProcesses.add(new InternalRoute(this::incrementProductRoute, "direct:increment-product"));

        // Register new processes as routes in the camel context
        RouteRegistrationUtil.registerRouteHandlers(utilityProcesses, routeDefinitionFunction);
    }

}
