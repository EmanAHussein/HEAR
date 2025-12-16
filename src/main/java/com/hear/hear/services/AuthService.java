package com.hear.hear.services;

import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.entities.User;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
private final UserRepository userRepository;

public User getCurrentUser(){
var authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
    assert authenticatedUser != null;
    var userId = (Integer) authenticatedUser.getPrincipal();
    assert userId != null;
    return userRepository.findById(userId).orElse(null);
}

}
