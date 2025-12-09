package com.enit.service;

import java.util.List;
import com.enit.entities.User;

import jakarta.ejb.Local;

@Local
public interface UserServiceLocal {

    User login(String username, String password);
    
    User loginByEmail(String email, String password); // ADD THIS

    void create(User user);

    User update(User user);

    void delete(Long id);

    User findById(Long id);

    User findByUsername(String username);
    
    User findByEmail(String email); // ADD THIS

    List<User> findAll();
}
