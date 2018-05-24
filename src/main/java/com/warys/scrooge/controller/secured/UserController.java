package com.warys.scrooge.controller.secured;

import com.warys.scrooge.command.account.PasswordCommand;
import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/me")
public final class UserController {

    private final UserService authenticatedUserService;

    UserController(UserService authenticatedUserService) {
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("")
    public ResponseEntity<UserCommand> getMe(@AuthenticationPrincipal final SessionUser user) {
        return new ResponseEntity<>(authenticatedUserService.getMe(user), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<UserCommand> updateMe
            (@AuthenticationPrincipal final SessionUser user, @Valid @RequestBody final UserCommand newUser) {
        return new ResponseEntity<>(authenticatedUserService.updateUser(user, newUser), HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<UserCommand> partialUpdateMe
            (@AuthenticationPrincipal final SessionUser user, @RequestBody final UserCommand partialNewUser) {
        UserCommand updatedUser = authenticatedUserService.partialUpdateUser(user, partialNewUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/activate/password")
    public ResponseEntity<String> updatePassword
            (@AuthenticationPrincipal final SessionUser user, @RequestBody final PasswordCommand password) {
        authenticatedUserService.updatePassword(user, password);
        return new ResponseEntity<>("Password updated", HttpStatus.OK);
    }
}