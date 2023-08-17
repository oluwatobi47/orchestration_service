package uk.ac.bcu.invorchestrator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.bcu.orderservice.proxy.OrderService;
import uk.ac.bcu.orderservice.proxy.OrderService_Service;

/**
 * Initializes Essential class objects which is made available in the Spring IOC container as dependencies
 */
@Configuration
public class BeanConfig {

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @Bean
    public OrderService orderServiceApi() {
        OrderService_Service service = new OrderService_Service();
        return service.getOrderServiceImplPort();
    }
}
