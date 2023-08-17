package uk.ac.bcu.invorchestrator.service;

import uk.ac.bcu.invorchestrator.dto.auth.SignInResponse;

public interface SessionContext {
    void setClientContext(SignInResponse signInResponse);
    SignInResponse getClientSessionInfo(String clientId);
}
