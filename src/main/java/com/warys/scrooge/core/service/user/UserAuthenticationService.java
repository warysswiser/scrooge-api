package com.warys.scrooge.core.service.user;

import com.warys.scrooge.command.account.LoginResponse;
import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.model.builder.UserBuilder;
import com.warys.scrooge.core.model.user.User;
import com.warys.scrooge.core.repository.UserRepository;
import com.warys.scrooge.core.service.notification.Notifier;
import com.warys.scrooge.infrastructure.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

@Service
public class UserAuthenticationService implements AuthenticationService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserAuthenticationService.class);

    private static final String SECRET = "ThisIsASecret";

    private final UserService publicUserService;
    private final UserRepository userRepository;
    private final Notifier mailNotifier;

    @Value("${app.auth.token.expiration.days}")
    private int expirationDays;


    public UserAuthenticationService(@Qualifier("publicUserService") UserService publicUserService, UserRepository userRepository, Notifier mailNotifier) {
        this.publicUserService = publicUserService;
        this.userRepository = userRepository;
        this.mailNotifier = mailNotifier;
    }

    @Override
    public LoginResponse login(final String email, final String password) throws ApiException {

        UserCommand user = publicUserService.getUserByCredentials(email, password);

        String token = Jwts.builder()
                .setSubject("authentication token")
                .setExpiration(Date.from(LocalDateTime.now().plusYears(expirationDays).toInstant(ZoneOffset.UTC)))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .claim("userId", user.getId())
                .compact();

        return new LoginResponse(token, user.getId(), user.getUsername(), user.getEmail());
    }

    @Override
    public UserCommand register(final UserCommand command) throws ApiException {
        String email = command.getEmail();

        publicUserService.checkUserEmail(email);

        final User user = new UserBuilder().with(
                o -> {
                    o.creationDate = LocalDateTime.now();
                    o.email = email;
                    o.firstName = command.getFirstName();
                    o.lastName = command.getLastName();
                    o.username = command.getUsername();
                    o.password = command.getPassword();
                }
        ).build();

        final var result = new UserCommand();

        final User createdUser = userRepository.insert(user);

        BeanUtil.copyBean(createdUser, result);

        mailNotifier.sendSubscriptionMessage(user.getEmail());

        return result;
    }

    @Override
    public Optional<UserCommand> findByToken(final String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            final String userId = claims.get("userId", String.class);
            if (null != userId && claims.getExpiration().after(new Date())) {
                var userPayload = new UserCommand();
                final User savedUser = userRepository.findById(userId).orElseThrow();
                BeanUtil.copyBean(savedUser, userPayload);
                return Optional.of(userPayload);
            }
        } catch (JwtException e) {
            logger.error("an error occurred", e);
        }
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(final String s) {
        return null;
    }
}
