package com.hear.hear.dtos;

import com.hear.hear.entities.Department;
import com.hear.hear.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
public class StudentProfileDto {
    private final Integer id;
    private final int studentCode;
    private final byte currentLevel;
    private final Department department;
    private final String name;
    public StudentProfileDto(Integer id, int studentCode, byte currentLevel, Department department,User user) {
        this.id = id;
        this.studentCode = studentCode;
        this.currentLevel = currentLevel;
        this.department = department;
        this.name = user.getName();
    }
}
