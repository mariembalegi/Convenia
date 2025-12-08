package com.enit.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "etablissements_partenaires")
public class EtablissementPartenaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String nom;
    
    @Column(length = 100)
    private String sigle;
    
    @Column(nullable = false, length = 100)
    private String pays;
    
    @Column(length = 100)
    private String ville;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeEtablissement type;
    
    @Column(columnDefinition = "TEXT")
    private String adresse;
    
    @Column(length = 20)
    private String telephone;
    
    @Column(length = 150)
    private String email;
    
    @Column(length = 200)
    private String siteWeb;
        
    @Column(nullable = false)
    private Boolean actif = true;
    
    @OneToMany(mappedBy = "etablissementPartenaire", cascade = CascadeType.ALL)
    private List<Demande> demandes = new ArrayList<>();
    
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;
    
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getSigle() { return sigle; }
    public void setSigle(String sigle) { this.sigle = sigle; }
    
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    
    public TypeEtablissement getType() { return type; }
    public void setType(TypeEtablissement type) { this.type = type; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSiteWeb() { return siteWeb; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }
        
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    
    public List<Demande> getDemandes() { return demandes; }
    public void setDemandes(List<Demande> demandes) { this.demandes = demandes; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { 
        this.dateCreation = dateCreation; 
    }
}

enum TypeEtablissement {
    UNIVERSITE,
    ECOLE_SUPERIEURE,
    INSTITUT_RECHERCHE,
    GRANDE_ECOLE,
    AUTRE
}