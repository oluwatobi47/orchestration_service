package uk.ac.bcu.invorchestrator.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data // Decorator facade for attaching getter and setter to generated class file at build time
public class UpdateClientDTO {
    private String id;
    private String name;
    private String email;
    private String address;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("business_type")
    private BusinessType businessType;
}
