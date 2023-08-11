package uk.ac.bcu.invorchestrator.orchestratorservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(info = @Info(title = "Orchestration Service", description = "Inventory System Orchestration Service", version = "1.0"))
public class SwaggerConfig {
//    Related Custom Bean definitions can be added to this configuration class
}
