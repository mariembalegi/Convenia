package com.enit.services;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import com.enit.entities.User;

import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class UserService implements UserServiceLocal {

    @PersistenceContext(unitName = "myPU")
    private EntityManager em;

    // ------------------------------
    // CREATE USER
    // ------------------------------
    @Override
    public void create(User user) {
        // Hash du mot de passe avant enregistrement
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);

        em.persist(user);
    }

    // ------------------------------
    // UPDATE USER
    // ------------------------------
    @Override
    public User update(User user) {
        // Si le mot de passe n'est pas encore hashé, on le hash
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashed);
        }
        return em.merge(user);
    }

    // ------------------------------
    // DELETE USER
    // ------------------------------
    @Override
    public void delete(Long id) {
        User u = em.find(User.class, id);
        if (u != null) {
            em.remove(u);
        }
    }

    // ------------------------------
    // FIND BY ID
    // ------------------------------
    @Override
    public User findById(Long id) {
        return em.find(User.class, id);
    }

    // ------------------------------
    // FIND USER BY USERNAME
    // ------------------------------
    @Override
    public User findByUsername(String username) {
        try {
            TypedQuery<User> q = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :x", User.class
            );
            q.setParameter("x", username);
            return q.getSingleResult();
        } catch (Exception e) {
            return null; // username non trouvé
        }
    }

    // ------------------------------
    // FIND ALL USERS
    // ------------------------------
    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class)
                 .getResultList();
    }

    // ------------------------------
    // LOGIN
    // ------------------------------
    @Override
    public User login(String username, String password) {
        User u = findByUsername(username);
        if (u == null) return null;

        // Vérifier le mot de passe hashé
        if (BCrypt.checkpw(password, u.getPassword())) {
            return u;
        }

        return null;
    }
}
