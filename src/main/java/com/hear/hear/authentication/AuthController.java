package com.hear.hear.authentication;

import com.hear.hear.Mappers.RegisterUserRequest;
import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.dtos.LoginRequest;
import com.hear.hear.dtos.RegisterRequest;
import com.hear.hear.entities.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    @Autowired
    RegisterUserRequest registerUserRequest;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request
    ) {
        var user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(),user.getHashedPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("email","Email is already registered"));
        }
        var user = registerUserRequest.toRegister(request);
        String hashed=passwordEncoder.encode(user.getHashedPassword());
        user.setHashedPassword(hashed);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
