package com.hear.hear.Controllers;

import com.hear.hear.Mappers.StudentMapping;
import com.hear.hear.Repositories.StudentRepository;
import com.hear.hear.dtos.StudentProfileDto;
import com.hear.hear.entities.Course;
import com.hear.hear.services.AuthService;
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
    private final AuthService authService;
    StudentRepository studentRepository;


    @GetMapping("/gitCourses")
    public ResponseEntity<Set<Course>> getCourses(){
        var courses=studentRepository.findCoursesForStudentByStudentId(authService.getCurrentUser().getId());
        if(courses==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(courses);
    }

}


















