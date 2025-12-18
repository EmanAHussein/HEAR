package com.hear.hear.dtos;

import com.hear.hear.entities.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterStudentProfile {

    @NotBlank(message = "student code is required")
    private int studentCode;
    @NotBlank(message = "current level is required")
    private byte currentLevel;
    @NotNull(message = "department is required")
    private Department department;
}
