package com.hear.hear.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserDto {
    private String name;
    @Email
    private String email;
    private String phone;
    private boolean isAdmin;
    private String newPassword;
}
