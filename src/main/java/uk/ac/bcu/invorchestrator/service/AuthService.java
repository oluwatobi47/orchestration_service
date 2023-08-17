package uk.ac.bcu.invorchestrator.service;

import org.apache.camel.model.RouteDefinition;

public interface AuthService {

    // Update profile not exposed in this implementation scope
    void handleUpdateProfile(RouteDefinition process) throws Exception;
    void handleSignIn(RouteDefinition process) throws Exception;
    void handleSignUp(RouteDefinition process) throws Exception;

}
