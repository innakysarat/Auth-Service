package ru.hse.actiongame.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.actiongame.config.PasswordConfig;
import ru.hse.actiongame.dto.UserInfoDto;
import ru.hse.actiongame.dto.UserLoginDto;
import ru.hse.actiongame.dto.UserRegisterDto;
import ru.hse.actiongame.exception.ApiException;
import ru.hse.actiongame.exception.ErrorCode;
import ru.hse.actiongame.model.User;
import ru.hse.actiongame.repository.UserRepository;
import ru.hse.actiongame.security.JwtTokenService;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordConfig passwordConfig;

    public String registerUser(UserRegisterDto userRegisterDto) {
        if (userRepository.findByUsername(userRegisterDto.getUsername()).isEmpty()) {
            var token = jwtTokenService.generateToken(userRegisterDto.getUsername());
            var encodedPassword = passwordConfig.passwordEncoder().encode(userRegisterDto.getPassword());
            var user = new User(userRegisterDto.getUsername(), encodedPassword, token, userRegisterDto.getPassword());
            userRepository.save(user);
            return token;
        } else {
            throw new ApiException(ErrorCode.ERROR_USER_ALREADY_EXISTS, "User with such id already exists");
        }
    }

    public String login(UserLoginDto userLoginDto) {
        var user = userRepository.findByUsername(userLoginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User with such username already exists"));
        if (passwordConfig.passwordEncoder().matches(userLoginDto.getPassword(), user.getPassword())) {
            return user.getToken();
        } else {
            throw new ApiException(ErrorCode.ERROR_INVALID_PASSWORD, "Invalid password or login");
        }

    }

    public UserInfoDto getUser(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.ERROR_USER_NOT_FOUND, "User with such id not found"));
        return UserInfoDto.builder()
                .username(username)
                .email(user.getEmail())
                .build();
    }
}
