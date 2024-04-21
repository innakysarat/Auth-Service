package ru.hse.actiongame.security;

import javax.naming.AuthenticationException;

public class AuthException extends AuthenticationException {
    public AuthException(String msg) {
        super(msg);
    }
}
