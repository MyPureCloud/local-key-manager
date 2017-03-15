package com.inin.keymanagement.models.dto;


/**
 * General error message with the status.
 */
public class ErrorMessage {
    private String message;
    private Integer status;

    public ErrorMessage(){}

    public ErrorMessage(String message, Integer status){
        this.message = message;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
