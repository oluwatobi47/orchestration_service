package uk.ac.bcu.invorchestrator.orchestratorservice.service;

import uk.ac.bcu.invorchestrator.orchestratorservice.dto.auth.SignInResponse;

public interface SessionContext {
    void setClientContext(SignInResponse signInResponse);
    SignInResponse getCurrentUserInfo(String clientId);
}
