package uk.ac.bcu.invorchestrator.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ProcessValidationException extends HttpStatusCodeException {

    private final String message;

    public ProcessValidationException(HttpStatus statusCode, String message) {
        super(statusCode, message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
