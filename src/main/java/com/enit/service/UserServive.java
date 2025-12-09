package com.enit.services;

import java.util.List;
import com.enit.entities.User;

import jakarta.ejb.Local;

@Local
public interface UserServiceLocal {

    User login(String username, String password);

    void create(User user);

    User update(User user);

    void delete(Long id);

    User findById(Long id);

    User findByUsername(String username);

    List<User> findAll();
}
