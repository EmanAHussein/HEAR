package com.hear.hear.dtos;

import com.hear.hear.entities.Department;
import lombok.Data;

@Data
public class StudentDto {

    private String name;
    private String email;
    private Integer userId;
    private Integer studentId;
    private int studentCode;
    private byte currentLevel;
    private Department department;

}
