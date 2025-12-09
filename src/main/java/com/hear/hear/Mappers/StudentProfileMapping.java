package com.hear.hear.Mappers;

import com.hear.hear.dtos.StudentProfileDto;
import com.hear.hear.entities.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentProfileMapping {
    StudentProfileDto ToStudentProfileDto(Student student);
}
