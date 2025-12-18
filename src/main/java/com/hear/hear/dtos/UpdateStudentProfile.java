package com.hear.hear.dtos;

import com.hear.hear.entities.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStudentProfile {

    private int studentCode;
    private byte currentLevel;
    private Department department;
}
