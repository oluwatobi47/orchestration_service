package uk.ac.bcu.invorchestrator.dto.auth;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * Enum definition for supported business types.
 * */
public enum BusinessType implements Serializable {
    SoftwareEngineering("Software Engineering"),
    Construction("Construction"),
    InformationTechnology("Information Technology");

    private final String value;

    BusinessType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
