package com.hear.hear.Mappers;

import com.hear.hear.dtos.StudentProfileDto;
import com.hear.hear.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StudentProfileMapping {
    StudentProfileDto ToStudentProfileDto(Student student);
}
