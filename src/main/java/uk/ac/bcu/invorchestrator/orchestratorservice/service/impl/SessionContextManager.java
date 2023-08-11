package uk.ac.bcu.invorchestrator.orchestratorservice.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.bcu.invorchestrator.orchestratorservice.dto.auth.SignInResponse;
import uk.ac.bcu.invorchestrator.orchestratorservice.service.SessionContext;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class SessionContextManager implements SessionContext {

    private Map<String, SignInResponse> inMemoryCache;

    @PostConstruct
    private void init() {
        inMemoryCache = new HashMap<>();
    }

    @Override
    public void setClientContext(SignInResponse signInResponse) {
        inMemoryCache.put(signInResponse.getClient().getId(), signInResponse);
    }

    @Override
    public SignInResponse getCurrentUserInfo(String clientId) {
        return inMemoryCache.get(clientId);
    }
}
