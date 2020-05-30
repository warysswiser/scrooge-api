package com.warys.scrooge.domain.service.user;

import com.warys.scrooge.application.command.response.LoginResponse;
import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.infrastructure.exception.ApiException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AuthenticationService extends UserDetailsService {

    LoginResponse login(String email, String password) throws ApiException;

    User register(User user) throws ApiException;

    Optional<User> findByToken(String token);
}