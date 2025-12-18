package com.hear.hear.services;


import com.hear.hear.Mappers.CourseMapping;
import com.hear.hear.Repositories.CourseRepository;
import com.hear.hear.dtos.RegisterCourseDto;
import com.hear.hear.dtos.UpdateCourseDto;
import com.hear.hear.entities.Course;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@AllArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapping registerCourseMapping;
    private final CourseMapping courseMapping;

    public Course register(RegisterCourseDto registerCourseDto) {
        if (courseRepository.existsByName(registerCourseDto.getName()) || courseRepository.existsByCode(registerCourseDto.getCode())){
            return null;
        }
        Course course = registerCourseMapping.toCourse(registerCourseDto);
        return courseRepository.save(course);
    }

    public Map<String, String> updateCourse(Integer id, UpdateCourseDto updateCourseDto) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        courseMapping.updateFromDto(updateCourseDto, course);
        courseRepository.save(course);
        return Map.of("message", "Course updated successfully");
    }

    public void deleteCourse(Integer id) {
        courseRepository.deleteById(id);
    }
}
