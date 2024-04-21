package ru.hse.actiongame.security;

import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.actiongame.repository.UserRepository;

@Service
@AllArgsConstructor
public class SecurityService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;

    public UserDetails authenticateByToken(@NotNull String token) throws AuthException {
        final var user = userRepository.findByToken(token)
                .orElseThrow(() -> new AuthException("Token does not exist"));

        final Claims claims = jwtTokenService.getClaims(token);
        final var username = claims.getSubject();
        if (!username.equals(user.getUsername())) {
            throw new AuthException("Token is not valid for user");
        }
        return new UserDetails(user, UserType.USER);
    }
}
