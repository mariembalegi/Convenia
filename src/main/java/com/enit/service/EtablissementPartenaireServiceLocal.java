// src/main/java/com/enit/service/EtablissementPartenaireServiceLocal.java
package com.enit.service;

import com.enit.entities.EtablissementPartenaire;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface EtablissementPartenaireServiceLocal {
    EtablissementPartenaire create(EtablissementPartenaire etab);
    List<EtablissementPartenaire> findAllActive();
    EtablissementPartenaire findById(Long id);
}