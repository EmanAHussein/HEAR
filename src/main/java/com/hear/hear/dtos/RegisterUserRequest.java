package com.hear.hear.dtos;

import com.fasterxml.jackson.databind.JsonNode;
import com.hear.hear.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {
    @NotBlank(message = "Email is required")
    @Email
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    private String name;
    @NotBlank(message = "phone is required")
    private String phone;
    @NotNull(message = "Role is required")
    private Role role;
    private boolean isAdmin;
    @NotNull(message = "Profile info is required")
    private Object profile;

}
