package uk.ac.bcu.invorchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import uk.ac.bcu.orderservice.proxy.OrderService_Service;

@SpringBootApplication
@EnableConfigurationProperties
public class OrchestratorServiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(OrchestratorServiceApplication.class, args);
//		OrderService_Service service = new OrderService_Service();
//        var orders = service.getOrderServiceImplPort().getAllOrders();
	}

}
