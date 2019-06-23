package com.warys.scrooge.infrastructure.filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.authentication.BadCredentialsException;

import javax.servlet.http.HttpServletRequest;

import static com.warys.scrooge.infrastructure.filter.TokenAuthenticationFilter.TOKEN_QUERY_PARAM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RunWith(PowerMockRunner.class)
public class TokenAuthenticationFilterShould {

    private static final String USER_TOKEN = "my token is here";

    @Test
    public void get_token_from_request_when_valid_token_is_set_on_header() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        TokenAuthenticationFilter tested = new TokenAuthenticationFilter(httpServletRequest -> true);
        when(request.getHeader(AUTHORIZATION)).thenReturn("Bearer " + USER_TOKEN);

        final String tokenFromRequest = tested.getTokenFromRequest(request);

        assertThat(tokenFromRequest).isEqualTo(USER_TOKEN);
    }

    @Test
    public void get_token_from_request_when_valid_token_is_set_on_query_param() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        TokenAuthenticationFilter tested = new TokenAuthenticationFilter(httpServletRequest -> true);
        when(request.getHeader(AUTHORIZATION)).thenReturn(null);
        when(request.getParameter(TOKEN_QUERY_PARAM)).thenReturn(USER_TOKEN);

        final String tokenFromRequest = tested.getTokenFromRequest(request);

        assertThat(tokenFromRequest).isEqualTo(USER_TOKEN);
    }


    @Test(expected = BadCredentialsException.class)
    public void trow_BadCredentialsException_when_token_is_not_set_on_header_and_query_param() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        TokenAuthenticationFilter tested = new TokenAuthenticationFilter(httpServletRequest -> true);
        when(request.getHeader(AUTHORIZATION)).thenReturn(null);
        when(request.getParameter(TOKEN_QUERY_PARAM)).thenReturn(null);

        final String tokenFromRequest = tested.getTokenFromRequest(request);

        assertThat(tokenFromRequest).isEqualTo(USER_TOKEN);
    }
}