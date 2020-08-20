package com.warys.scrooge.application.rest;

import com.warys.scrooge.application.command.request.LoginRequest;
import com.warys.scrooge.application.command.request.RegisterRequest;
import com.warys.scrooge.application.command.response.LoginResponse;
import com.warys.scrooge.application.command.response.RegisterResponse;
import com.warys.scrooge.domain.service.user.AuthenticationService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/public")
public final class PublicUsersController {

    private final AuthenticationService authentication;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) throws ApiException {
        LoginResponse loginResponse = authentication.login(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(loginResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) throws ApiException {
        return new ResponseEntity<>(authentication.register(request), HttpStatus.CREATED);
    }
}
