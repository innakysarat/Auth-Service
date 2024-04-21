package ru.hse.actiongame.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    ERROR_USER_NOT_FOUND("USER_NOT_FOUND", "По данному идентификатору пользователь не найден"),
    ERROR_NO_ACCESS_TO_RESOURCE("NO_ACCESS_TO_RESOURCE", "Недостаточно прав для доступа к ресурсу"),
    ERROR_USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "Пользователь с таким логином уже существует"),
    ERROR_INVALID_PASSWORD("INVALID_PASSWORD", "Невалидный пароль или логин")
    ;

    private final String code;
    private final String message;
}
