package com.pos.backend.service;

import org.springframework.stereotype.Service;

import com.pos.backend.entity.User;

@Service
public interface UserService {
    User createUser(User user);

    User getUserByUserName(String name);
}
