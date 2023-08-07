package com.project.spring.service;

import com.project.spring.model.AppUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<AppUser> findUserById(Long id);
}
