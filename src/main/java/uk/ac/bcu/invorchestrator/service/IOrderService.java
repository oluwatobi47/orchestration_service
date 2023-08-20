package uk.ac.bcu.invorchestrator.service;

import org.apache.camel.model.RouteDefinition;

import java.util.function.Function;

public interface IOrderService {
    /**
     * Handles the business logic to place an order
     * Involves cross service functions involving the SOAP based order web service and the REST based warehouse service
     *
     * @param routeDefinition The camel context route definition
     */
    public void handlePlaceOrder(RouteDefinition routeDefinition);

    /**
     * Handles the business logic to cancel an order
     * Involves cross service functions involving the SOAP based order web service and the REST based warehouse service
     *
     * @param routeDefinition The camel context route definition
     */
    public void handleCancelOrder(RouteDefinition routeDefinition);

    /**
     * Handles the business logic to complete/fulfill an order
     *
     * @param routeDefinition The camel context route definition
     */
    public void handleCompleteOrder(RouteDefinition routeDefinition);

    /**
     * Handles the business logic to retrieve all the orders for a given client
     *
     * @param routeDefinition The camel context route definition
     */
    public void handleGetOrders(RouteDefinition routeDefinition);

    /**
     * Handles registration of internalized utility service functions involved
     * in EIP communication and order service related business logic
     *
     * @param routeDefinitionFunction Camel context from function that takes in a process
     *                                id string and returns a route definition
     */
    public void registerAdditionalHandlers(Function<String, RouteDefinition> routeDefinitionFunction);
}
