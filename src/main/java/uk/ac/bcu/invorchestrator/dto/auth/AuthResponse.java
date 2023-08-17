package uk.ac.bcu.invorchestrator.dto.auth;

import lombok.Data;


/**
 * Model representation of api responses from the auth service
 * @param <T> Generic Placeholder for Entity Type being returned
 */
@Data
public class AuthResponse<T> {
    private String status;
    private String message;
    private T data;
}
