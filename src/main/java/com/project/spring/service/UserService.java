package com.project.spring.service;

import com.project.spring.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findUserById(Long id);
}