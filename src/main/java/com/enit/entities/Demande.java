package com.enit.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "demandes_conventions")
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String reference;
    
    @Column(nullable = false, length = 200)
    private String titre;
    
 
    @Column(name = "type_convention", nullable = false)
    private String typeConvention;
    
    @Column(columnDefinition = "TEXT")
    private String objectif;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    

    @Column(columnDefinition = "TEXT")
    private String commentaires;
    
 
    @Column(nullable = false)
    private String statut;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "etablissement_partenaire_id", nullable = false)
    private EtablissementPartenaire etablissementPartenaire;
    
    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PieceJointe> piecesJointes = new ArrayList<>();
    
    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();
    
    @Column(name = "date_soumission")
    private LocalDateTime dateSoumission;
    
    @Column(name = "date_derniere_modification")
    private LocalDateTime dateDerniereModification;
    
    @Column(name = "date_validation_finale")
    private LocalDateTime dateValidationFinale;
    
    @PrePersist
    protected void onCreate() {
        dateSoumission = LocalDateTime.now();
        dateDerniereModification = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        dateDerniereModification = LocalDateTime.now();
    }
    
   
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getTypeConvention() { return typeConvention; }
    public void setTypeConvention(String typeConvention) { 
        this.typeConvention = typeConvention; 
    }
    
    public String getObjectif() { return objectif; }
    public void setObjectif(String objectif) { this.objectif = objectif; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    
    public String getCommentaires() { return commentaires; }
    public void setCommentaires(String commentaires) { this.commentaires = commentaires; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public User getUser() { return user; }
    public void setEnseignantChercheur(User user) { 
        this.user = user; 
    }
    
    public EtablissementPartenaire getEtablissementPartenaire() { 
        return etablissementPartenaire; 
    }
    public void setEtablissementPartenaire(EtablissementPartenaire etablissementPartenaire) { 
        this.etablissementPartenaire = etablissementPartenaire; 
    }
    
    public List<PieceJointe> getPiecesJointes() { return piecesJointes; }
    public void setPiecesJointes(List<PieceJointe> piecesJointes) { 
        this.piecesJointes = piecesJointes; 
    }
    
    
    public List<Notification> getNotifications() { return notifications; }
    public void setNotifications(List<Notification> notifications) { 
        this.notifications = notifications; 
    }
    
    public LocalDateTime getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(LocalDateTime dateSoumission) { 
        this.dateSoumission = dateSoumission; 
    }
    
    public LocalDateTime getDateDerniereModification() { return dateDerniereModification; }
    public void setDateDerniereModification(LocalDateTime dateDerniereModification) { 
        this.dateDerniereModification = dateDerniereModification; 
    }
    
    public LocalDateTime getDateValidationFinale() { return dateValidationFinale; }
    public void setDateValidationFinale(LocalDateTime dateValidationFinale) { 
        this.dateValidationFinale = dateValidationFinale; 
    }
}

