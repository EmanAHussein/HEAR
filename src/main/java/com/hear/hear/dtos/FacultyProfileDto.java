package com.hear.hear.dtos;

import com.hear.hear.entities.Department;
import com.hear.hear.entities.User;
import lombok.Getter;
@Getter
public class FacultyProfileDto {
    private final String name;
    private String jobTitle;
    private String scientificDegree;
    private String bio;

    public FacultyProfileDto(String jobTitle, String scientificDegree, String bio, User user) {
        this.name=user.getName();
        this.jobTitle=jobTitle;
        this.scientificDegree=scientificDegree;
        this.bio=bio;
    }
}
