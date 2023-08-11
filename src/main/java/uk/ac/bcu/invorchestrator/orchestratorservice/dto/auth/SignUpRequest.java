package uk.ac.bcu.invorchestrator.orchestratorservice.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpRequest {

    private String name;
    private String email;
    private String address;
    private String password;
    private String confirmPassword;
    private BusinessType businessType;
    private String apiKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("confirm_password")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    @JsonProperty("confirm_password")
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @JsonProperty("business_type")
    public BusinessType getBusinessType() {
        return businessType;
    }

    @JsonProperty("business_type")
    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    @JsonProperty("api_key")
    public String getApiKey() {
        return apiKey;
    }

    @JsonProperty("api_key")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
