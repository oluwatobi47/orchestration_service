package uk.ac.bcu.invorchestrator.orchestratorservice.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.support.DefaultMessage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uk.ac.bcu.invorchestrator.orchestratorservice.config.ServiceEndpoints;
import uk.ac.bcu.invorchestrator.orchestratorservice.dto.DataResponse;
import uk.ac.bcu.invorchestrator.orchestratorservice.dto.OrderDTO;
import uk.ac.bcu.invorchestrator.orchestratorservice.dto.warehouse.ProductValidationRequest;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderRoute extends RouteBuilder {

    private final ServiceEndpoints endpoints;

    public OrderRoute(ServiceEndpoints endpoints) {
        this.endpoints = endpoints;
    }

    private void registerOrderBusinessProcessHandlers() throws Exception {

    }

    // Build logic to handle all order related business processes.
    private void handlePlaceOrder() throws Exception {

        var route = from("direct:place-order");
//                route.process(this::validateOrderItems)
                route.choice()
                .when(predicate -> {
                    var validationResponse = predicate.getIn().getBody();
                    return false;
                });

        var routeDef = from("/order/place");
//        routeDef.getInput().getEndpoint().
    }


    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet");
//                .bindingMode(RestBindingMode.auto);

        rest()
                .consumes(MediaType.APPLICATION_JSON_VALUE).produces(MediaType.APPLICATION_JSON_VALUE)
                .post("/order/place").to("direct:place-order")
                .get("/order/{orderId}").to("direct:validate-items")
                .get("/order/cancel/{orderId}").to("direct:validate-items")
                .get("/order").to("direct:validate-items");

        // TODO: Create function in order service for define and register all order related workflows.
        // Service class will be responsible for injection of endpoint dependency as well as utilization
        // of other reusable functions
        // Function Name: registerProcessHandlers
        //
    }




}
