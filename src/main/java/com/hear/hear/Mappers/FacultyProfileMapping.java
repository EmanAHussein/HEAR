package com.hear.hear.Mappers;

import com.hear.hear.dtos.FacultyProfileDto;
import com.hear.hear.dtos.RegisterFacultyProfile;
import com.hear.hear.dtos.UpdateFacultyProfile;
import com.hear.hear.dtos.UpdateStudentProfile;
import com.hear.hear.entities.FacultyMember;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

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
}
