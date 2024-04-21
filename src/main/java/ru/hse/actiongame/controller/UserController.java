package ru.hse.actiongame.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.actiongame.dto.UserInfoDto;
import ru.hse.actiongame.dto.UserRegisterDto;
import ru.hse.actiongame.dto.UserLoginDto;
import ru.hse.actiongame.service.UserService;

import static ru.hse.actiongame.security.AuthenticationUtils.getCurrentAuthentication;

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
    public String registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        return userService.registerUser(userRegisterDto);
    }

    @GetMapping
    public UserInfoDto getUser() {
        var username = getCurrentAuthentication().getName();
        return userService.getUser(username);
    }
}
