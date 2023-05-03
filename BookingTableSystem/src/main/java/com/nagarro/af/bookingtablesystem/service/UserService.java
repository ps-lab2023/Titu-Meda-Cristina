package com.nagarro.af.bookingtablesystem.service;

import com.nagarro.af.bookingtablesystem.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService<T extends UserDTO> {
    T save(T t);

    T findById(UUID id);

    T findByEmail(String email);

    T findByUsername(String username);

    List<T> findAll();

    void delete(UUID id);
}
