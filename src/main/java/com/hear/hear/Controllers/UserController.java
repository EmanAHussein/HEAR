package com.hear.hear.Controllers;

import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.entities.Student;
import com.hear.hear.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    UserRepository userRepository;
    @GetMapping("/get_users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
