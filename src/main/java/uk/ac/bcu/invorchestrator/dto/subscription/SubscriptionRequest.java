package uk.ac.bcu.invorchestrator.dto.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SubscriptionRequest {
    @JsonProperty("client_id")
    private String clientId;
    private String plan;
}
