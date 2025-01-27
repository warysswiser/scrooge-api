package com.warys.scrooge.infrastructure.config;

import com.warys.scrooge.application.rest.auth.filter.TokenAuthenticationFilter;
import com.warys.scrooge.application.rest.auth.provider.TokenAuthenticationProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/public/**"
    };

    private static final RequestMatcher PROTECTED_URLS = new AntPathRequestMatcher("/**");

    private final TokenAuthenticationProvider provider;

    SecurityConfig(final TokenAuthenticationProvider provider) {
        super();
        this.provider = requireNonNull(provider);
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .exceptionHandling()
                .and()
                .authenticationProvider(provider)
                .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();

        http.cors();
    }

    @Bean
    public TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
        var filter = new TokenAuthenticationFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler successHandler() {
        var successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new NoRedirectStrategy());
        return successHandler;
    }

    /**
     * Disable Spring boot automatic filter registration.
     */
    @Bean
    FilterRegistrationBean disableAutoRegistration(final TokenAuthenticationFilter filter) {
        var registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(UNAUTHORIZED);
    }
}
