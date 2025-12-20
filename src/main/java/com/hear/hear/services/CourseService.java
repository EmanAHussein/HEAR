package com.hear.hear.services;


import com.hear.hear.Mappers.CourseMapping;
import com.hear.hear.Repositories.CourseRepository;
import com.hear.hear.dtos.RegisterCourseDto;
import com.hear.hear.dtos.UpdateCourseDto;
import com.hear.hear.entities.Course;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@AllArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapping registerCourseMapping;
    private final CourseMapping courseMapping;

//    public Course register(RegisterCourseDto registerCourseDto) {
//        if (courseRepository.existsByName(registerCourseDto.getName()) || courseRepository.existsByCode(registerCourseDto.getCode())){
//            return null;
//        }
//        Course course = registerCourseMapping.toCourse(registerCourseDto);
//        return courseRepository.save(course);
//    }

    public Course register(RegisterCourseDto dto) throws BadRequestException {
        if (courseRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Course name already exists");
        }
        if (courseRepository.existsByCode(dto.getCode())) {
            throw new BadRequestException("Course code already exists");
        }

        Course course = registerCourseMapping.toCourse(dto);
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
