package com.hear.hear.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterCourseDto {

    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "code is required")
    private String code;
    @NotBlank(message = "credit hours is required")
    private byte creditHours;
    private String description;
}
