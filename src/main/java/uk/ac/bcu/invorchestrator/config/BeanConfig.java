package uk.ac.bcu.invorchestrator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.bcu.orderservice.proxy.OrderService;
import uk.ac.bcu.orderservice.proxy.OrderService_Service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Initializes Essential class objects which is made available in the Spring IOC container as dependencies
 */
@Configuration
public class BeanConfig {

    @Value("${inv.service.order}")
    private String orderServiceEndpoint;

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @Bean
    public OrderService orderServiceApi() {
        OrderService_Service service = null;
        try {
            service = new OrderService_Service(new URL(String.format("%s/OrderService?wsdl", orderServiceEndpoint)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        var port = service.getOrderServiceImplPort();
        return port;
    }
}
