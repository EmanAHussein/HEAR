package com.hear.hear.dtos;

import com.hear.hear.entities.Role;
import lombok.Data;

@Data
public class AdminUpdateUserProfile {
    private Role role;

    private UpdateStudentProfile updateStudentProfile;
    private UpdateFacultyProfile updateFacultyProfile;
}
