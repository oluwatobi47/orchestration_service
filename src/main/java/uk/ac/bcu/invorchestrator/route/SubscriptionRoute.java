package uk.ac.bcu.invorchestrator.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uk.ac.bcu.invorchestrator.dto.DataResponse;
import uk.ac.bcu.invorchestrator.dto.subscription.SubscriptionPaymentDTO;
import uk.ac.bcu.invorchestrator.dto.subscription.SubscriptionRequest;
import uk.ac.bcu.invorchestrator.service.SubscriptionService;

@Component
public class SubscriptionRoute extends RouteBuilder {

    private final SubscriptionService subscriptionService;

    public SubscriptionRoute(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    private void handleSubscriptionProcesses() {
        this.subscriptionService.getServicePlans(from("direct:get-subscription-plans"));
        this.subscriptionService.handleGetClientSubscription(from("direct:get-client-subscription"));
        this.subscriptionService.handleGetClientPayment(from("direct:get-client-payments"));
        this.subscriptionService.handleSubscribeToPlan(from("direct:subscribe"));
        this.subscriptionService.handleSubscriptionPayment(from("direct:pay"));
    }

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .enableCORS(true)
                .component("servlet")
                .bindingMode(RestBindingMode.json);


        // Define endpoints and reference to request handlers
        rest()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get("/account/plans").to("direct:get-subscription-plans")
                .get("/account/subscription/{clientId}").to("direct:get-client-subscription")
                .get("/account/payments/{clientId}").to("direct:get-client-payments")
                .post("/account/subscribe").type(SubscriptionRequest.class).to("direct:subscribe")
                .post("/account/pay").type(SubscriptionPaymentDTO.class).to("direct:pay")
                .outType(DataResponse.class);

        this.handleSubscriptionProcesses();
    }
}
