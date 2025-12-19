package com.hear.hear.authentication;

import com.hear.hear.Mappers.UserMapping;
import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.dtos.JwtResponse;
import com.hear.hear.dtos.LoginRequest;
import com.hear.hear.dtos.RegisterUserRequest;
import com.hear.hear.dtos.UserDto;
import com.hear.hear.services.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final UserService userService;

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

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {

                            @ExampleObject(
                                    name = "Student Register",
                                    summary = "Register a student",
                                    value = """
                {
                  "email": "student@example.com",
                  "password": "string",
                  "name": "Student Name",
                  "phone": "0100000000",
                  "role": "STUDENT",
                  "profile": {
                    "studentCode": 123456,
                    "currentLevel": 3,
                    "department": "CS"
                  },
                  "admin": false
                }
                """
                            ),

                            @ExampleObject(
                                    name = "Faculty Register",
                                    summary = "Register a faculty member",
                                    value = """
                {
                  "email": "faculty@example.com",
                  "password": "string",
                  "name": "Faculty Name",
                  "phone": "0100000000",
                  "role": "FACULTYMEMBER",
                  "profile": {
                    "jobTitle": "Professor",
                    "department": "CS",
                    "scientificDegree": "PhD",
                    "bio": "Some bio"
                  },
                  "admin": false
                }
                """
                            )
                    }
            )
    )

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        var user = userService.register(request);
        if(user == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("Duplicated Error","Email or Phone are already registered"));
        }
        var userDto=userMapping.toDto(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        Integer userId;

        if (principal instanceof Number) {
            userId = ((Number) principal).intValue(); // handles Long, Integer, etc.
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(userMapping.toDto(user));
    }

}
