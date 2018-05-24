package com.warys.scrooge.infrastructure.exception.business;

import com.warys.scrooge.infrastructure.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ElementNotFoundException extends ApiException {

    public ElementNotFoundException(String s) {
        super(s);
    }
}
