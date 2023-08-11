package uk.ac.bcu.invorchestrator.orchestratorservice.service;

import org.apache.camel.Exchange;
import org.apache.camel.model.ProcessDefinition;
import org.apache.camel.model.RouteDefinition;

public interface AuthService {

    void handleSignIn(RouteDefinition process) throws Exception;
    void signIn(Exchange exchange) throws Exception;
    void getClientInformation(Exchange exchange) throws Exception;
    void signUp(Exchange exchange) throws Exception;
    void signOut(Exchange exchange) throws Exception;

}
