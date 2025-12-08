package com.enit.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String titre;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeNotification type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id")
    private Demande demande;
    
    @Column(name = "destinataire_email", nullable = false)
    private String destinataireEmail;
    
    @Column(nullable = false)
    private Boolean envoyee = false;
    
    @Column(nullable = false)
    private Boolean lue = false;
    
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;
    
    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;
    
    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;
    
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
    
  
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public TypeNotification getType() { return type; }
    public void setType(TypeNotification type) { this.type = type; }
    
    public Demande getDemande() { return demande; }
    public void setDemande(Demande demande) { this.demande = demande; }
    
    public String getDestinataireEmail() { return destinataireEmail; }
    public void setDestinataireEmail(String destinataireEmail) { 
        this.destinataireEmail = destinataireEmail; 
    }
    
    public Boolean getEnvoyee() { return envoyee; }
    public void setEnvoyee(Boolean envoyee) { 
        this.envoyee = envoyee;
        if (envoyee && dateEnvoi == null) {
            dateEnvoi = LocalDateTime.now();
        }
    }
    
    public Boolean getLue() { return lue; }
    public void setLue(Boolean lue) { 
        this.lue = lue;
        if (lue && dateLecture == null) {
            dateLecture = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { 
        this.dateCreation = dateCreation; 
    }
    
    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
    
    public LocalDateTime getDateLecture() { return dateLecture; }
    public void setDateLecture(LocalDateTime dateLecture) { 
        this.dateLecture = dateLecture; 
    }
}

enum TypeNotification {
    CONFIRMATION_SOUMISSION,
    INSTRUCTION_EN_COURS,
    MODIFICATION_REQUISE,
    VALIDATION_SERVICE,
    REJET,
    VALIDATION_FINALE,
    RAPPEL,
    AUTRE
}