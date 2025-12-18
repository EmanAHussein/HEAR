package com.hear.hear.Mappers;

import com.hear.hear.dtos.RegisterStudentProfile;
import com.hear.hear.dtos.StudentProfileDto;
import com.hear.hear.dtos.UpdateStudentProfile;
import com.hear.hear.entities.Student;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface StudentProfileMapping {
    Student toStudent(RegisterStudentProfile registerStudentProfile);
    Student toStudent(UpdateStudentProfile updateStudentProfile);
    StudentProfileDto ToStudentProfileDto(Student student);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void updateFromDto(
            UpdateStudentProfile studentProfileDto,
            @MappingTarget Student student
    );
}
