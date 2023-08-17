package uk.ac.bcu.invorchestrator.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Initializes the Open api documentation for all available endpoints
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Orchestration Service", description = "Inventory System Orchestration Service", version = "1.0"))
public class SwaggerConfig {
//    Related Custom Bean definitions can be added to this configuration class
}
