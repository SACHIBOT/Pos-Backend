package com.pos.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pos.backend.entity.User;

@Service
public interface UserService {
    User createUser(User user);

    User getUserByUserName(String name);

    List<User> getAllUsers();

    void deleteUser(Long id);

    User updateUserRole(Long userId, Long roleId);
}
