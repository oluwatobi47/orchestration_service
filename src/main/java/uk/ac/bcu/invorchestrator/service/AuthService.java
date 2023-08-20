package uk.ac.bcu.invorchestrator.service;

import org.apache.camel.model.RouteDefinition;

public interface AuthService {

    // Update profile not exposed in this implementation scope
    void handleUpdateProfile(RouteDefinition process) throws Exception;

    /**
     * Handles the business process for authenticating a client user
     * @param routeDefinition The camel route
     */
    void handleSignIn(RouteDefinition routeDefinition) throws Exception;


    /**
     * Handles the business process for registering and creating client customer account,
     * involves the auth service and the notification service
     * @param routeDefinition The camel route
     */
    void handleSignUp(RouteDefinition routeDefinition) throws Exception;

}
