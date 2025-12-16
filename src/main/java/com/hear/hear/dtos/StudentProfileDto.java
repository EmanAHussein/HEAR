package com.hear.hear.dtos;

import com.hear.hear.entities.Department;
import com.hear.hear.entities.User;
import lombok.Getter;

@Getter
public class StudentProfileDto {
    private final String name;
    private final int studentCode;
    private final byte currentLevel;
    private final Department department;

    public StudentProfileDto(int studentCode, byte currentLevel, Department department,User user) {
        this.name = user.getName();
        this.studentCode = studentCode;
        this.currentLevel = currentLevel;
        this.department = department;
    }
}
