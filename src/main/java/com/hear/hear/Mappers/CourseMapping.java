package com.hear.hear.Mappers;

import com.hear.hear.dtos.RegisterCourseDto;
import com.hear.hear.dtos.UpdateCourseDto;
import com.hear.hear.entities.Course;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CourseMapping {
    Course toCourse(RegisterCourseDto registerCourseDto);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void updateFromDto(
            UpdateCourseDto updateCourseDto,
            @MappingTarget Course course
    );

    RegisterCourseDto toDto(Course course);
    Set<RegisterCourseDto> toDto(Set<Course> courses);
}
