package com.enit.service;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import com.enit.entities.User;

@Stateless
public class UserService implements UserServiceLocal {

    @PersistenceContext(unitName = "myPU")
    private EntityManager em;

    // CREATE USER
    @Override
    public void create(User user) {
        // Pas de hashage, on enregistre le mot de passe tel quel
        em.persist(user);
    }

    // UPDATE USER
    @Override
    public User update(User user) {
        // On met à jour directement
        return em.merge(user);
    }

    // DELETE
    @Override
    public void delete(Long id) {
        User u = em.find(User.class, id);
        if (u != null) {
            em.remove(u);
        }
    }

    // FIND BY ID
    @Override
    public User findById(Long id) {
        return em.find(User.class, id);
    }

    // FIND USER BY USERNAME
    @Override
    public User findByUsername(String username) {
        try {
            TypedQuery<User> q = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :x", User.class
            );
            q.setParameter("x", username);
            return q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // FIND ALL
    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class)
                 .getResultList();
    }

    // LOGIN without hash
    @Override
    public User login(String username, String password) {
        User u = findByUsername(username);

        if (u == null) return null;

        // Comparaison directe (sans hash)
        if (u.getPassword().equals(password)) {
            return u;
        }

        return null;
    }
}
