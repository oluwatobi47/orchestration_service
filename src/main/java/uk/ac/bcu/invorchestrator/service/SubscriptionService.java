package uk.ac.bcu.invorchestrator.service;

import org.apache.camel.model.RouteDefinition;

public interface SubscriptionService {
    /**
     * Handles the business process for retrieving all available service plans
     * @param routeDefinition The camel route
     */
    void getServicePlans(RouteDefinition routeDefinition);

    /**
     * Handles the business process for a client subscribing to a service plan
     * @param routeDefinition The camel route
     */
    void handleSubscribeToPlan(RouteDefinition routeDefinition);

    /**
     * Handles the business process for a client making payment for a service plan
     * Process involves the subscription and the notification services
     * @param routeDefinition The camel route
     */
    void handleSubscriptionPayment(RouteDefinition routeDefinition);

    /**
     * Handles the business process for retrieving details about a clients current subscription plan
     * @param routeDefinition The camel route
     */
    void handleGetClientSubscription(RouteDefinition routeDefinition);

    /**
     * Handles the business process for retrieving details for client payments
     * @param routeDefinition The camel route
     */
    void handleGetClientPayment(RouteDefinition routeDefinition);
}
