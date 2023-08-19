package uk.ac.bcu.invorchestrator.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uk.ac.bcu.invorchestrator.dto.order.OrderRequest;
import uk.ac.bcu.invorchestrator.service.IOrderService;

@Component
public class OrderRoute extends RouteBuilder {

    private final IOrderService orderService;

    public OrderRoute(IOrderService orderService) {
        this.orderService = orderService;
    }

    // Build logic to handle all order related business processes.
    private void handleOrderRelatedBusinessLogic() {
        this.orderService.handlePlaceOrder(from("direct:place-order"));
        this.orderService.handleCancelOrder(from("direct:cancel-order"));
        this.orderService.handleGetOrders(from("direct:get-client-orders"));
        this.orderService.registerAdditionalHandlers(this::from);
    }

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .enableCORS(true)
                .component("servlet")
                .bindingMode(RestBindingMode.auto);

        rest()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .post("/order/place").to("direct:place-order").type(OrderRequest.class)
                .patch("/order/cancel/{orderRef}").to("direct:cancel-order")
                .get("/orders/{clientId}").to("direct:get-client-orders");

        this.handleOrderRelatedBusinessLogic();
    }
}
