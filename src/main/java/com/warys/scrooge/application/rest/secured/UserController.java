package com.warys.scrooge.application.rest.secured;

import com.warys.scrooge.application.command.request.UpdatePassword;
import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.domain.service.user.UserService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/me")
@Api(description = "API to manage users")
public final class UserController {

    private final UserService authenticatedUserService;

    @GetMapping("")
    public ResponseEntity<User> getMe(@AuthenticationPrincipal final Session user) throws ApiException {
        return new ResponseEntity<>(authenticatedUserService.getMe(user), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<User> updateMe
            (@AuthenticationPrincipal final Session user, @RequestBody @Valid final User newUser) throws ApiException {
        return new ResponseEntity<>(authenticatedUserService.updateUser(user, newUser), HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<User> partialUpdateMe
            (@AuthenticationPrincipal final Session user, @RequestBody @Valid final User partialNewUser) throws ApiException {
        User updatedUser = authenticatedUserService.partialUpdateUser(user, partialNewUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword
            (@AuthenticationPrincipal final Session user, @RequestBody @Valid final UpdatePassword password) throws ApiException {
        authenticatedUserService.updatePassword(user, password);
        return new ResponseEntity<>("Password updated", HttpStatus.OK);
    }
}