package ru.hse.actiongame.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterDto {
    public String username;
    public String password;
    public String email;
}
