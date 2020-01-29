package com.everysport.api.sdk;

import java.io.IOException;
import java.util.List;

public class EverysportException extends IOException {

    private com.everysport.api.domain.api.Error error;
    private int status;
    private Throwable cause;

    public EverysportException(String message) {
        super(message);
    }

    public EverysportException(Throwable t) {
        super(t.getMessage());
        this.cause = t;
    }

    public EverysportException(com.everysport.api.domain.api.Error error, int status) {
        super();
        this.error = error;
        this.status = status;
    }

    public EverysportException(com.everysport.api.domain.api.Error error, int status, String message) {
        super(message);
        this.error = error;
        this.status = status;
    }

    public com.everysport.api.domain.api.Error getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }

    public String getMessages() {
        return error != null ? formatMessages() : super.getMessage();
    }

    public Throwable getCause() {
        return this.cause;
    }

    private String formatMessages() {

        List<String> messages = error.getMessages();
        StringBuilder stringBuilder = new StringBuilder();
        for (String message : messages) {
            stringBuilder.append(message).append("\n");
        }

        return stringBuilder.toString();
    }
}
