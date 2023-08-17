package uk.ac.bcu.invorchestrator.dto.subscription;

import lombok.Data;

@Data
public class SubscriptionPlanDTO {
    private String id;
    private String planName;
    private String description;
    private Integer days;
    private Double cost;
}
