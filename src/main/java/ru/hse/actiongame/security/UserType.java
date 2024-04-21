package ru.hse.actiongame.security;

public enum UserType {
    USER, ADMIN;

    public String getAuthority() {
        return "ROLE_" + this;
    }
}
