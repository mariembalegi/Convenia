package com.enit.service;

import com.enit.entities.Demande;
import com.enit.entities.User;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface DemandeServiceLocal {
    Demande createDemande(Demande demande, User user);
    List<Demande> findDemandesByEnseignant(User user);
    Demande findById(Long id);
    Demande updateDemande(Demande demande);
}