package com.warys.scrooge.domain.exception;

import com.warys.scrooge.infrastructure.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InconsistentElementException extends ApiException {

    public InconsistentElementException(String s) {
        super(s);
    }
}
