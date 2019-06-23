package com.warys.scrooge.infrastructure.provider;

import com.warys.scrooge.core.service.user.AuthenticationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final AuthenticationService auth;

    public TokenAuthenticationProvider(AuthenticationService auth) {
        this.auth = auth;
    }


    @Override
    protected void additionalAuthenticationChecks(final UserDetails userDetails, final UsernamePasswordAuthenticationToken auth) {

    }

    @Override
    protected UserDetails retrieveUser(
            final String username,
            final UsernamePasswordAuthenticationToken authentication) {
        final Object token = authentication.getCredentials();
        return Optional
                .ofNullable(token)
                .map(String::valueOf)
                .flatMap(auth::findByToken)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with authentication token=" + token));
    }
}
