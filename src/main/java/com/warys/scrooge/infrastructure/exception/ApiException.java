package com.warys.scrooge.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ApiException extends Exception {

    protected ApiException(String s) {
        super(s);
    }

    protected ApiException(Throwable cause) {
        super(cause);
    }
}
