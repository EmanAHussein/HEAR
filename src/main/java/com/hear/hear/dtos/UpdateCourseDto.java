package com.hear.hear.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCourseDto {

    private String name;
    private String code;
    private byte creditHours;
    private String description;
}
