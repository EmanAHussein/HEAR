package com.hear.hear.Mappers;

import com.hear.hear.dtos.FacultyProfileDto;
import com.hear.hear.entities.FacultyMember;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacultyMemberProfileMapping {
    FacultyProfileDto ToFacultyProfileDto(FacultyMember facultyMember);
}
