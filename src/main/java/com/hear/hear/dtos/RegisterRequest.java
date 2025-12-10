package com.hear.hear.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class RegisterRequest {
    @NotBlank(message = "Email is required")
    @Email
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    private String name;

}
