package uk.ac.bcu.invorchestrator.orchestratorservice.dto.auth;

public enum BusinessType {
    SoftwareEngineering("Software Engineering"),
    Construction("Construction"),
    InformationTechnology("Information Technology");

    private final String value;

    BusinessType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
