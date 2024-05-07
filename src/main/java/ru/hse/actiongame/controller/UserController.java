package ru.hse.actiongame.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.actiongame.dto.*;
import ru.hse.actiongame.service.UserService;

import java.util.List;

@RestController
@RequestMapping("action-game/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @PostMapping
    public String registerUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.registerUser(userRequestDto);
    }

    @GetMapping("/info")
    public UserResponseDto getUser() {
        return userService.getUser();
    }

    @PutMapping("/{username}")
    public UserResponseDto updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUser(username, userRequestDto);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }

    @PutMapping("/statistics")
    public UserStatisticsResponseDto updateUserStatistics(@RequestBody UserStatisticsRequestDto dto) {
        return userService.updateUserStatistics(dto);
    }

    @GetMapping("/statistics")
    public List<UserStatisticsResponseDto> getUsersStatistics() {
        return userService.getUsersStatistics();
    }
}
