package ru.hse.actiongame.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hse.actiongame.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetails {
    private String username;
    private UserType userType;

    public UserDetails(User user, UserType userType) {
        this.username = user.getUsername();
        this.userType = userType;
    }
}
