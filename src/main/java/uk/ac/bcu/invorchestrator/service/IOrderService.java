package uk.ac.bcu.invorchestrator.service;

import org.apache.camel.model.RouteDefinition;

import java.util.function.Function;

public interface IOrderService {
    public void handlePlaceOrder(RouteDefinition routeDefinition);
    public void handleCancelOrder(RouteDefinition routeDefinition);
    public void handleGetOrders(RouteDefinition routeDefinition);
    public void registerAdditionalHandlers(Function<String, RouteDefinition> routeDefinitionFunction);
}
