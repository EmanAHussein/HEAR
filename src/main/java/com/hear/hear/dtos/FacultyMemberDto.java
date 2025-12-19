package com.hear.hear.dtos;

import com.hear.hear.entities.Department;
import lombok.Data;

@Data
public class FacultyMemberDto {

    private String name;
    private String email;
    private Integer userId;
    private Integer facultyMemberId;
    private String jobTitle;
    private String scientificDegree;
    private Department department;
    private String bio;

}
