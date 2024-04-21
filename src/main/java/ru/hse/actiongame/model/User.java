package ru.hse.actiongame.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
