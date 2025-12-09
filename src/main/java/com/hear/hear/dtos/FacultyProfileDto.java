package com.hear.hear.dtos;

import com.hear.hear.entities.Department;
import com.hear.hear.entities.User;
import lombok.Getter;

@Getter
public class FacultyProfileDto implements ProfileDto {
    private final String name;
    private final String jobTitle;
    private final Department department;
    private final String scientificDegree;
    private final String bio;

    public FacultyProfileDto(String jobTitle,Department department, String scientificDegree, String bio, User user) {
        this.name=user.getName();
        this.jobTitle=jobTitle;
        this.department=department;
        this.scientificDegree=scientificDegree;
        this.bio=bio;
    }
}
