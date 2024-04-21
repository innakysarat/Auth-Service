package ru.hse.actiongame.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthProvider implements AuthenticationProvider {
    private final SecurityService securityService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final var gameAuthentication = (ActionGameAuthentication) authentication;
        final var token = gameAuthentication.getToken();
        final var maskedToken = ObjectUtils.isEmpty(token) ? null : token.substring(token.length() - 6);
        try {
            final var userDetails = switch (gameAuthentication.getUserDetails().getUserType()) {
                case USER -> securityService.authenticateByToken(token);
                default ->
                        throw new IllegalStateException("Unexpected value: " + gameAuthentication.getUserDetails().getUserType());
            };
            if (ObjectUtils.isEmpty(userDetails)) {
                log.error("Security service returned null");
                throw new AuthException("NOT_AUTHENTICATED");
            }
            log.info("User: {}, Token=****{}", userDetails, maskedToken);
            return new ActionGameAuthentication(userDetails, token);
        } catch (Exception e) {
            log.error("Security service internal error: {}", e.getMessage());
            throw new RuntimeException("NOT_AUTHENTICATED");
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
