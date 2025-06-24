package com.resume.resume_parser.response;

import java.time.LocalDateTime;

public class Response<T> {

    private boolean success;
    private String message;
    private T payload;
    private LocalDateTime timestamp;

    public Response() {
        this.timestamp = LocalDateTime.now();
    }

    public Response(boolean success, String message, T payload) {
        this.success = success;
        this.message = message;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
