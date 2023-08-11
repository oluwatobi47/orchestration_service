package uk.ac.bcu.invorchestrator.orchestratorservice.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.bcu.invorchestrator.orchestratorservice.dto.DataResponse;

@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping
    public DataResponse<String> getOrder() throws Exception {
        return new DataResponse<>("Sample Order");
    }
}
