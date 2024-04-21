package ru.hse.actiongame.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {
    public String username;
    public String email;
}
