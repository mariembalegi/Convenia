package com.enit.service;

import com.enit.entities.Demande;
import com.enit.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.Year;
import java.util.List;

@Stateless
public class DemandeService implements DemandeServiceLocal {

    @PersistenceContext(unitName = "myPU")
    private EntityManager em;

    @Override
    public Demande createDemande(Demande demande, User user) {
        demande.setEnseignantChercheur(user);
        demande.setReference(genererReference());
        demande.setStatut(demande.getStatut());
        em.persist(demande);
        return demande;
    }

    @Override
    public List<Demande> findDemandesByEnseignant(User user) {
        return em.createQuery(
            "SELECT d FROM Demande d WHERE d.user = :user ORDER BY d.dateSoumission DESC",
            Demande.class)
            .setParameter("user", user)
            .getResultList();
    }

    @Override
    public Demande findById(Long id) {
        return em.find(Demande.class, id);
    }

    @Override
    public Demande updateDemande(Demande demande) {
        return em.merge(demande);
    }

    public String genererReference() {
        String prefix = "CONV";
        String annee = String.valueOf(java.time.Year.now().getValue());
        Long count = em.createQuery(
            "SELECT COUNT(d) FROM Demande d WHERE d.reference LIKE :prefix", Long.class)
            .setParameter("prefix", prefix + "-" + annee + "%")
            .getSingleResult();
        return prefix + "-" + annee + "-" + String.format("%04d", count + 1);
    }
}