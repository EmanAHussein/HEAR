package com.hear.hear.dtos;

import com.hear.hear.entities.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterFacultyProfile {

    @NotBlank(message = "job title is required")
    private String jobTitle;
    @NotNull(message = "department is required")
    private Department department;
    @NotBlank(message = "scientific degree is required")
    private String scientificDegree;
    private String bio;
}
