package com.enit.entities;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;   // login unique (email ou identifiant LDAP)

    @Column(nullable = false)
    private String password;   // mot de passe hashé (BCrypt)

    @Column(nullable = false)
    private String fullName;   // nom complet de l’enseignant-chercheur

    @Column(nullable = false)
    private String role;       // "ENSEIGNANT", "DRI", "ADMIN"

    @Column(nullable = false)
    private boolean active = true;  // compte activé ou non

    public User() {}

    public User(String username, String password, String fullName, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    // Getters & setters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
