
package com.enit.service;

import com.enit.entities.EtablissementPartenaire;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class EtablissementPartenaireService implements EtablissementPartenaireServiceLocal {

    @PersistenceContext(unitName = "myPU")
    private EntityManager em;

    @Override
    public EtablissementPartenaire create(EtablissementPartenaire etab) {
        etab.setActif(true);
        em.persist(etab);
        return etab;
    }

    @Override
    public List<EtablissementPartenaire> findAllActive() {
        return em.createQuery(
                "SELECT e FROM EtablissementPartenaire e WHERE e.actif = true ORDER BY e.nom",
                EtablissementPartenaire.class)
                .getResultList();
    }

    @Override
    public EtablissementPartenaire findById(Long id) {
        return em.find(EtablissementPartenaire.class, id);
    }
}