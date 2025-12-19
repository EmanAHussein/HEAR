package com.hear.hear.Mappers;

import com.hear.hear.dtos.*;
import com.hear.hear.entities.FacultyMember;
import com.hear.hear.entities.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacultyProfileMapping {
    FacultyMember toFacultyMember(RegisterFacultyProfile registerFacultyProfile);

    FacultyProfileDto ToFacultyProfileDto(FacultyMember facultyMember);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void updateFromDto(
            UpdateFacultyProfile updateFacultyProfile,
            @MappingTarget FacultyMember facultyMember
    );

    FacultyMember toFacultyMember(UpdateFacultyProfile updateFacultyProfile);

    @Mapping(source = "id", target = "facultyMemberId")
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.id", target = "userId")
    FacultyMemberDto toDto(FacultyMember facultyMembers);
    List<FacultyMemberDto> toDto(List<FacultyMember> facultyMembers);
}
