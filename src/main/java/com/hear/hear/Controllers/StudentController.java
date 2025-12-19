package com.hear.hear.Controllers;

//import com.hear.hear.Mappers.StudentAllScheduleMapping;
import com.hear.hear.Mappers.CourseMapping;
import com.hear.hear.Repositories.StudentRepository;
import com.hear.hear.authentication.AuthService;
import com.hear.hear.dtos.RegisterCourseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
public class StudentController {

    StudentRepository studentRepository;
    CourseMapping courseMapping;
    private final AuthService authService;

    @GetMapping("/courses/get")
    public ResponseEntity<Set<RegisterCourseDto>> getCourses(){
        var courses= courseMapping.toDto(studentRepository.findCoursesForStudentByStudentId(authService.getCurrentUser().getId()));
        if(courses==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(courses);
    }

}


















