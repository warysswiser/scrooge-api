package com.warys.scrooge.core.service.user;

import com.warys.scrooge.command.account.LoginResponse;
import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.infrastructure.exception.ApiException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AuthenticationService extends UserDetailsService {

    LoginResponse login(String email, String password) throws ApiException;

    UserCommand register(UserCommand user) throws ApiException;

    Optional<UserCommand> findByToken(String token);
}