package uk.ac.bcu.invorchestrator.orchestratorservice.service.impl;

import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultMessage;
import org.springframework.stereotype.Service;
import uk.ac.bcu.invorchestrator.orchestratorservice.dto.DataResponse;
import uk.ac.bcu.invorchestrator.orchestratorservice.dto.OrderDTO;
import uk.ac.bcu.invorchestrator.orchestratorservice.dto.warehouse.ProductValidationRequest;
import uk.ac.bcu.invorchestrator.orchestratorservice.service.OrderService;

import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {


    private void sampleFunction() {
//        from("rest:order")
//                .outputType(DataResponse.class)
//                .process(this::placeOrder)
//                .aggregate().exchange().end()
//                .to(endpoints.getAuth())
//                .choice().when();
    }

    private void validateOrderItems(Exchange exchange) {
        var orderRequest = exchange.getIn().getBody(OrderDTO.class);
        var productValidationRequests = orderRequest.getItems().stream()
                .map(obj -> new ProductValidationRequest(obj, 1))
                .collect(Collectors.toList());
        var message = new DefaultMessage(exchange);
        message.setBody(productValidationRequests);
        exchange.setMessage(message);
    }

    // Create Service class for this
    private void placeOrder(Exchange exchange) {

    }

    @Override
    public void handlePlaceOrder(Exchange exchange) throws Exception {
        var orderRequest = exchange.getIn().getBody(OrderDTO.class);
        var message = new DefaultMessage(exchange);
        message.setBody(orderRequest);
        exchange.setMessage(message);
    }
}
