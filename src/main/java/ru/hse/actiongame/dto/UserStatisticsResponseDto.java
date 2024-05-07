package ru.hse.actiongame.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatisticsResponseDto {
    protected String username;
    protected Long deaths;
    protected Long kills;
}
