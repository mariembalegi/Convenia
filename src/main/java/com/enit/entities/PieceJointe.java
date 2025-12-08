package com.enit.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pieces_jointes")
public class PieceJointe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom_fichier", nullable = false, length = 255)
    private String nomFichier;
    
    @Column(name = "chemin_fichier", nullable = false, length = 500)
    private String cheminFichier;
    
    @Column(name = "type_fichier", length = 100)
    private String typeFichier;
    
    @Column(name = "taille_fichier")
    private Long tailleFichier;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_document")
    private TypeDocument typeDocument;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;
    
    @Column(name = "date_upload")
    private LocalDateTime dateUpload;
    
    @PrePersist
    protected void onCreate() {
        dateUpload = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    
    public String getNomFichier() { return nomFichier; }
    public void setNomFichier(String nomFichier) { this.nomFichier = nomFichier; }
    
    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { 
        this.cheminFichier = cheminFichier; 
    }
    
    public String getTypeMime() { return typeFichier; }
    public void setTypeMime(String typeMime) { this.typeFichier = typeMime; }
    
    public Long getTailleFichier() { return tailleFichier; }
    public void setTailleFichier(Long tailleFichier) { 
        this.tailleFichier = tailleFichier; 
    }
    
    public TypeDocument getTypeDocument() { return typeDocument; }
    public void setTypeDocument(TypeDocument typeDocument) { 
        this.typeDocument = typeDocument; 
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Demande getDemande() { return demande; }
    public void setDemande(Demande demande) { this.demande = demande; }
    
    public LocalDateTime getDateUpload() { return dateUpload; }
    public void setDateUpload(LocalDateTime dateUpload) { this.dateUpload = dateUpload; }
}

enum TypeDocument {
    MODELE_CONVENTION,
    PROJET_CONVENTION,
    ACCORD_CADRE,
    ANNEXE,
    BILAN_ACTION,
    COURRIER_MOTIVATION,
    CV_COORDONNATEUR,
    AUTRE
}