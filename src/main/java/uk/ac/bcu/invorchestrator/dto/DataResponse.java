package uk.ac.bcu.invorchestrator.dto;

import java.io.Serializable;

/**
 * Model representation for all api responses from the Orchestration Service
 * */
public class DataResponse<T> implements Serializable {
    private T data;
    private boolean valid;
    private String message;

    public DataResponse(T data){
        this.data = data;
        this.valid = true;
        this.message = "success";
    }

    public DataResponse(T data, boolean valid) {
        this.data = data;
        this.valid = valid;
    }

    public DataResponse(T data, boolean valid, String message) {
        this.data = data;
        this.valid = valid;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
