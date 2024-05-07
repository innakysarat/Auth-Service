package ru.hse.actiongame.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatisticsRequestDto {
    protected Long deaths;
    protected Long kills;
}
