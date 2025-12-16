package com.hear.hear.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hear.hear.entities.Department;
import com.hear.hear.entities.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FacultyProfileDto {

    private  String jobTitle;
    @NotBlank(message = "department is required")
    private  Department department;
    private  String scientificDegree;
    private  String bio;

    public FacultyProfileDto(String jobTitle,Department department, String scientificDegree, String bio) {

        this.jobTitle=jobTitle;
        this.department=department;
        this.scientificDegree=scientificDegree;
        this.bio=bio;
    }
}
