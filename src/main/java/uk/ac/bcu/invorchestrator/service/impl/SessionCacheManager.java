package uk.ac.bcu.invorchestrator.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.bcu.invorchestrator.dto.auth.SignInResponse;
import uk.ac.bcu.invorchestrator.service.SessionContext;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles an in memory cache for storing client details when signed in. (An external cache layer service is more ideal for such operation)
 */
@Service
public class SessionCacheManager implements SessionContext {

    private final Map<String, SignInResponse> inMemoryCache = new HashMap<>();

    @Override
    public void setClientContext(SignInResponse signInResponse) {
        inMemoryCache.put(signInResponse.getClient().getId(), signInResponse);
    }

    @Override
    public SignInResponse getClientSessionInfo(String clientId) {
        return inMemoryCache.get(clientId);
    }
}
