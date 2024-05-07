package ru.hse.actiongame.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hse.actiongame.config.PasswordConfig;
import ru.hse.actiongame.dto.*;
import ru.hse.actiongame.exception.ApiException;
import ru.hse.actiongame.exception.ErrorCode;
import ru.hse.actiongame.model.User;
import ru.hse.actiongame.model.UserStatistics;
import ru.hse.actiongame.repository.UserRepository;
import ru.hse.actiongame.repository.UserStatisticsRepository;
import ru.hse.actiongame.security.JwtTokenService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.hse.actiongame.security.AuthenticationUtils.getCurrentAuthentication;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserStatisticsRepository userStatisticsRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordConfig passwordConfig;

    public String registerUser(UserRequestDto userRequestDto) {
        if (userRepository.findByUsername(userRequestDto.getUsername()).isEmpty()) {
            var token = jwtTokenService.generateToken(userRequestDto.getUsername());
            var encodedPassword = passwordConfig.passwordEncoder().encode(userRequestDto.getPassword());
            var user = new User(userRequestDto.getUsername(), encodedPassword, token, userRequestDto.getEmail());
            var statistics = new UserStatistics(0L, 0L, user);
            user.setStatistics(statistics);
            userRepository.save(user);
            userStatisticsRepository.save(statistics);
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

    public UserResponseDto getUser() {
        var username = getCurrentAuthentication().getName();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.ERROR_USER_NOT_FOUND, "User with such id not found"));
        return UserResponseDto.builder()
                .username(username)
                .email(user.getEmail())
                .build();
    }

    public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.ERROR_USER_NOT_FOUND, "User with such id not found"));
        if (!getCurrentAuthentication().getName().equals(username)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(403), "Нет прав на доступ к ресурсу");
        }
        user.setPassword(userRequestDto.password);
        user.setEmail(userRequestDto.email);
        userRepository.save(user);
        return UserResponseDto.builder()
                .username(username)
                .email(user.getEmail())
                .build();
    }

    public void deleteUser(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.ERROR_USER_NOT_FOUND, "User with such id not found"));
        if (!getCurrentAuthentication().getName().equals(username)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(403), "Нет прав на доступ к ресурсу");
        }
        userRepository.delete(user);
    }

    public UserStatisticsResponseDto updateUserStatistics(UserStatisticsRequestDto dto) {
        var username = getCurrentAuthentication().getName();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.ERROR_USER_NOT_FOUND, "User with such id not found"));
        var userStatistics = user.getStatistics();
        userStatistics.setCountDeaths(userStatistics.getCountDeaths() + dto.getDeaths());
        userStatistics.setCountKills(userStatistics.getCountKills() + dto.getKills());
        user.setStatistics(userStatistics);
        userStatisticsRepository.save(userStatistics);
        userRepository.save(user);
        return UserStatisticsResponseDto.builder()
                .username(username)
                .deaths(userStatistics.getCountDeaths())
                .kills(userStatistics.getCountKills())
                .build();
    }

    public List<UserStatisticsResponseDto> getUsersStatistics() {
        var usersStats = userStatisticsRepository.findAll();
        var topUsers = usersStats.stream()
                .sorted(Comparator.comparing(UserStatistics::getCountKills, Comparator.reverseOrder())
                        .thenComparing(UserStatistics::getCountDeaths))
                .limit(10)
                .toList();

        return topUsers.stream()
                .map(userStat -> UserStatisticsResponseDto.builder()
                        .username(userStat.getUser().getUsername())
                        .deaths(userStat.getCountDeaths())
                        .kills(userStat.getCountKills())
                        .build())
                .collect(Collectors.toList());

    }
}
