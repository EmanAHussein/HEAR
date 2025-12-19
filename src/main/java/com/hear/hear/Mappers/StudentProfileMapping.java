package com.hear.hear.Mappers;

import com.hear.hear.dtos.RegisterStudentProfile;
import com.hear.hear.dtos.StudentDto;
import com.hear.hear.dtos.StudentProfileDto;
import com.hear.hear.dtos.UpdateStudentProfile;
import com.hear.hear.entities.Student;
import org.mapstruct.*;

import java.util.List;

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


    @Mapping(source = "id", target = "studentId")
    @Mapping(source = "studentCode", target = "studentCode")
    @Mapping(source = "currentLevel", target = "currentLevel")
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.id", target = "userId")
    StudentDto toDto(Student student);
    List<StudentDto> toDto(List<Student> students);

}
