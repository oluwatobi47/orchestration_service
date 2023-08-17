package uk.ac.bcu.invorchestrator.util;

import org.apache.camel.model.RouteDefinition;
import uk.ac.bcu.invorchestrator.dto.InternalRoute;

import java.util.List;
import java.util.function.Function;

public class RouteRegistrationUtil {
    public static void registerRouteHandlers(List<InternalRoute> routes, Function<String, RouteDefinition> function) {
        routes.forEach(route -> {
            try {
                route.getHandlerFunction().accept(function.apply(route.getRouteName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
