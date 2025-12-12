package com.enit.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Une seule table pour tous les types
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING) // colonne role
public abstract class User implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
<<<<<<< HEAD
    private String role;       // "ENSEIGNANT", "DRI", "ADMIN"

    public User() {}

    public User(String username,String email, String password, String fullName, String role) {
        this.username = username;
        this.email=email;
        this.password = password;
        this.role = role;
=======
    private String password;

    // ===== Constructeurs =====
    public User() {}

    public User(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
>>>>>>> 317ac65629f21f3349e70fb3d57d8aae62cb8d03
    }

    // ===== Getters & Setters =====
    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
<<<<<<< HEAD


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
=======
}
>>>>>>> 317ac65629f21f3349e70fb3d57d8aae62cb8d03
