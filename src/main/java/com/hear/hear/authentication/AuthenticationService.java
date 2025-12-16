package com.hear.hear.authentication;

import com.hear.hear.Mappers.RegisterUserRequest;
import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.dtos.LoginRequest;
import com.hear.hear.dtos.LoginResponse;
import com.hear.hear.dtos.RegisterRequest;
import com.hear.hear.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    RegisterUserRequest registerUserRequest;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    public Jwt refreshAccessToken(String refreshToken) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            throw new BadCredentialsException("Refresh token invalid.");
        }
        var userId = jwt.getUserId();
        var user = userRepository.findById(userId).orElseThrow();
        return jwtService.generateAccessToken(user);
    }

    User registerUser(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            return null;
        }
        var user = registerUserRequest.toUser(registerRequest);
        System.out.println(user.getPassword());
        String hashed=passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed);
        System.out.println(user.getPassword());
        userRepository.save(user);
        return user;
    }
}
