package uk.ac.bcu.invorchestrator.dto.warehouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Model representation of api responses from the warehouse service
 * @param <T> Generic Placeholder for Entity Type being returned
 */
@Data
public class WarehouseResponse<T> {
    @JsonProperty("status_code")
    private String statusCode;
    private String message;
    private T data;
}
