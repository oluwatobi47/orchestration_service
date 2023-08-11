package uk.ac.bcu.invorchestrator.orchestratorservice.service;

import org.apache.camel.Exchange;
import org.apache.camel.model.ProcessDefinition;

public interface OrderService {
    /**
     * Handlers for create order business process
     * @throws Exception Exception
     */
    public void handlePlaceOrder(Exchange exchange) throws Exception;
}
