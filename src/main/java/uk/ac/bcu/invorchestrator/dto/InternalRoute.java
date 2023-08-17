package uk.ac.bcu.invorchestrator.dto;

import lombok.Data;
import org.apache.camel.model.RouteDefinition;
import java.util.function.Consumer;

/**
 * This class represents a route and handler function pair definition.
 *
 * @apiNote The handler function represents the business logic execution while the routeName, is mapped to that function
 * Any process execution referencing the routeName will execute the corresponding route function
 */
@Data
public class InternalRoute {
    Consumer<RouteDefinition> handlerFunction;
    String routeName;

    public InternalRoute(Consumer<RouteDefinition> handlerFunction, String routeName) {
        this.handlerFunction = handlerFunction;
        this.routeName = routeName;
    }
}
