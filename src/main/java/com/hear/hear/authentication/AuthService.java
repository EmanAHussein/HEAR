package com.hear.hear.authentication;

import com.hear.hear.Mappers.UserMapping;
import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.dtos.LoginRequest;
import com.hear.hear.dtos.LoginResponse;
import com.hear.hear.dtos.RegisterUserRequest;
import com.hear.hear.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    UserMapping registerUserRequestMapper;

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

        var user = userRepository.findById(jwt.getUserId().intValue()).orElseThrow();
        return jwtService.generateAccessToken(user);
    }

    User registerUser(RegisterUserRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            return null;
        }
        var user = registerUserRequestMapper.toUser(registerRequest);
        String hashed=passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed);
        userRepository.save(user);
        return user;
    }

    public User getCurrentUser(){
        var authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        assert authenticatedUser != null;
        var userId = (Integer) authenticatedUser.getPrincipal();
        assert userId != null;
        return userRepository.findById(userId).orElse(null);
    }
}
