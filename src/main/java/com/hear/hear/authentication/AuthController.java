package com.hear.hear.authentication;

import com.hear.hear.Mappers.UserMapping;
import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.dtos.JwtResponse;
import com.hear.hear.dtos.LoginRequest;
import com.hear.hear.dtos.RegisterUserRequest;
import com.hear.hear.dtos.UserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtConfig jwtConfig;
    private final AuthService authService;
    private JwtService jwtService;
    private UserRepository userRepository;
    private final UserMapping userMapping;

    @PostMapping("/login")
    public JwtResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        var loginResult = authService.login(request);

        var refreshToken = loginResult.getRefreshToken().toString();
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");

        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new JwtResponse(loginResult.getAccessToken().toString());
    }

    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String token) {
        return jwtService.validateToken(token);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        Jwt accessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponse(accessToken.toString());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        var user = authService.registerUser(request);
        if(user == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("email","Email is already registered"));
        }
        var userDto=userMapping.toDto(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        var userId= authentication.getPrincipal();
        var user=userRepository.findById((Integer) userId).orElse(null);
        if(user==null) {
            return ResponseEntity.notFound().build();
        }
        var userDto=userMapping.toDto(user);
        return ResponseEntity.ok(userDto);
    }

}
