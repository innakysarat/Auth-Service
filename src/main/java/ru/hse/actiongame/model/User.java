package ru.hse.actiongame.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "GAME_USER")
public class User extends BaseEntity {
    @Column(name = "USERNAME")
    protected String username;

    @Column(name = "PASSWORD")
    protected String password;

    @Column(name = "TOKEN")
    protected String token;

    @Column(name = "EMAIL")
    protected String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected UserStatistics statistics;

    public User(String username, String encodedPassword, String token, String email) {
        this.username = username;
        this.password = encodedPassword;
        this.token = token;
        this.email = email;
    }
}
