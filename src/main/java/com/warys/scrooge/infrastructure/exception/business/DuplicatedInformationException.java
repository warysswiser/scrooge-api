package com.warys.scrooge.infrastructure.exception.business;

import com.warys.scrooge.infrastructure.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicatedInformationException extends ApiException {

    public DuplicatedInformationException(String s) {
        super(s);
    }
}
