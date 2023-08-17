package uk.ac.bcu.invorchestrator.service;

import org.apache.camel.model.RouteDefinition;
import java.util.function.Function;

public interface WarehouseService {
    void handleGetProduct(RouteDefinition process);
    void handleGetAllClientProducts(RouteDefinition process);
    void handleAddProduct(RouteDefinition process);
    void handleUpdateProduct(RouteDefinition process);
    void handleDeleteProduct(RouteDefinition process);
    void registerAdditionalHandlers(Function<String, RouteDefinition> routeDefinitionFunction);
}
