package com.warys.scrooge.infrastructure.exception.business.auth;

import com.warys.scrooge.infrastructure.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
class InvalidAccountException extends ApiException {

    public InvalidAccountException(String s) {
        super(s);
    }
}
