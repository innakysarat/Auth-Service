package ru.hse.actiongame.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.ObjectUtils.isEmpty;

public class AuthFilter extends AbstractAuthenticationProcessingFilter {
    public AuthFilter(final RequestMatcher requiresAuth, AuthenticationManager authenticationManager) {
        super(requiresAuth);
        this.setAuthenticationManager(authenticationManager);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ActionGameAuthentication authentication;
        final var authHeader = request.getHeader(AUTHORIZATION);
        if (isEmpty(authHeader)) {
            throw new RuntimeException("NOT_AUTHENTICATED");
        }
        final var token = authHeader.replace("Bearer", "").trim();
        authentication = new ActionGameAuthentication();
        authentication.setToken(token);
        authentication.getUserDetails().setUserType(UserType.USER);

        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                            final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
        if (!isEmpty(authResult) && authResult.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authResult);
        }
        chain.doFilter(request, response);
    }
}
