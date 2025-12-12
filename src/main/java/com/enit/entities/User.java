package com.enit.entities;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;   // login unique (email ou identifiant LDAP)

    @Column(nullable = false, unique = true)
    private String email;  // ADDED THIS - was missing!
    
    @Column(nullable = false)
    private String password;   // mot de passe hashé (BCrypt)

    @Column(nullable = false)
    private String role;       // "ENSEIGNANT", "DRI", "ADMIN"

    public User() {}

    public User(String username,String email, String password, String fullName, String role) {
        this.username = username;
        this.email=email;
        this.password = password;
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
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
