package uk.ac.bcu.invorchestrator.dto.subscription;

import lombok.Data;

import java.util.Date;

@Data
public class SubscriptionDTO {
    private String clientId;
    private Date endDate;
}
