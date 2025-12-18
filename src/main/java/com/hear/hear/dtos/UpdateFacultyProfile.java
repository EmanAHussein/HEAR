package com.hear.hear.dtos;

import com.hear.hear.entities.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateFacultyProfile {

    private String jobTitle;
    private Department department;
    private String scientificDegree;
    private String bio;
}
