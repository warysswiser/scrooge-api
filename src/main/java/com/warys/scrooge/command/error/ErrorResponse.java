package com.warys.scrooge.command.error;

import java.io.Serializable;
import java.util.Date;

public class ErrorResponse implements Serializable {
    private Date timestamp;
    private String message;
    private String exception;
    private String path;

    public ErrorResponse() {
        super();
    }

    public ErrorResponse(Date timestamp, String message, String path, String exception) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.exception = exception;
        this.path = path;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
