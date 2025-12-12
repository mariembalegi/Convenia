package com.enit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("ENSEIGNANT") // valeur stockée dans la colonne role
public class EnseignantChercheur extends User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "departement")
    private String departement;

    public EnseignantChercheur() {}

    public EnseignantChercheur(String fullName, String email, String password, String departement) {
        super(fullName, email, password);
        this.departement = departement;
    }

    // Getters & setters
    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }
}