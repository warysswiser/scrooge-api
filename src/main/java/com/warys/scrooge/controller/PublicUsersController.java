package com.warys.scrooge.controller;

import com.warys.scrooge.command.account.LoginCommand;
import com.warys.scrooge.command.account.LoginResponse;
import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.service.user.AuthenticationService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/public")
public final class PublicUsersController {

    private final AuthenticationService authentication;

    PublicUsersController(AuthenticationService authentication) {
        this.authentication = authentication;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginCommand request) throws ApiException {
        LoginResponse loginResponse = authentication.login(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(loginResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<UserCommand> register(@Valid @RequestBody UserCommand request) throws ApiException {
        return new ResponseEntity<>(authentication.register(request), HttpStatus.CREATED);
    }
}
