package com.warys.scrooge.domain.service.user;

import com.warys.scrooge.application.command.request.RegisterRequest;
import com.warys.scrooge.application.command.response.LoginResponse;
import com.warys.scrooge.application.command.response.RegisterResponse;
import com.warys.scrooge.domain.model.builder.UserBuilder;
import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.repository.mongo.UserRepository;
import com.warys.scrooge.infrastructure.repository.mongo.entity.UserDocument;
import com.warys.scrooge.infrastructure.spi.auth.TokenProvider;
import com.warys.scrooge.infrastructure.spi.notifier.Notifier;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static com.warys.scrooge.domain.common.util.Patcher.patch;

@Service
@Slf4j
public class UserAuthenticationService implements AuthenticationService {

    private final UserService publicUserService;
    private final UserRepository userRepository;
    private final Notifier mailNotifier;
    private final TokenProvider tokenProvider;


    public UserAuthenticationService(@Qualifier("publicUserService") UserService publicUserService, UserRepository userRepository, Notifier mailNotifier, TokenProvider tokenProvider) {
        this.publicUserService = publicUserService;
        this.userRepository = userRepository;
        this.mailNotifier = mailNotifier;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public LoginResponse login(final String email, final String password) throws ApiException {

        User user = publicUserService.getUserByCredentials(email, password);

        String token = tokenProvider.generateFrom(user);

        return LoginResponse
                .builder()
                .token(token)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    public RegisterResponse register(final RegisterRequest command) throws ApiException {
        String email = command.getEmail();

        publicUserService.checkUserEmail(email);

        final UserDocument user = new UserBuilder().with(
                o -> {
                    o.creationDate = LocalDateTime.now();
                    o.email = email;
                    o.firstName = command.getFirstName();
                    o.lastName = command.getLastName();
                    o.username = command.getUsername();
                }
        ).build();

        final var result = new RegisterResponse();

        final UserDocument createdUser = userRepository.insert(user);
        mailNotifier.sendSubscriptionMessage(user.getEmail());

        patch(createdUser, result);

        return result;
    }

    @Override
    public Optional<User> findByToken(final String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(tokenProvider.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
            final String userId = claims.get("userId", String.class);

            if (null != userId && claims.getExpiration().after(new Date())) {
                var userPayload = new User();
                final UserDocument savedUser = userRepository.findById(userId).orElseThrow();
                patch(savedUser, userPayload);
                return Optional.of(userPayload);
            }
        } catch (JwtException e) {
            log.error("an error occurred", e);
        }
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(final String s) {
        return null;
    }
}
