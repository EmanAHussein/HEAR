package com.hear.hear.Controllers;

import com.hear.hear.Mappers.RegisterUserRequest;
import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.dtos.ProfileDto;
import com.hear.hear.entities.Department;
import com.hear.hear.entities.Student;
import com.hear.hear.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RegisterUserRequest registerRequest;

    @GetMapping("/get_users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }



//    @GetMapping("/me")
//    public ResponseEntity<ProfileDto> getCurrentUser(){
//        Integer userId = 1;
//        var user = userRepository.findById(userId).orElse(null);
//        if(user == null){
//            return ResponseEntity.notFound().build();
//        }
//        FIGURED OUT I NEED TO IMPLEMENT Tokens & AUTH FIRST :(
//    }

}
