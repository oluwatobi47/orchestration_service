package uk.ac.bcu.invorchestrator.dto.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SubscriptionPaymentData {
    @JsonProperty("PayRef")
    String payRef;
    String amount;
    String date;
    String id;
    String planName;
}
