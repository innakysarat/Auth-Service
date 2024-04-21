package ru.hse.actiongame.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {
    public static ActionGameAuthentication getCurrentAuthentication(){
        return (ActionGameAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
