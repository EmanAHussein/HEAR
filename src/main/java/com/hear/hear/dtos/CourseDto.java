package com.hear.hear.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseDto {
    private int id;
    private String name;
    private String code;
    private byte creditHours;
    private String description;
}
