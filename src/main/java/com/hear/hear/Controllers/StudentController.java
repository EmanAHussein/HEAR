package com.hear.hear.Controllers;

import com.hear.hear.Mappers.StudentProfileMapping;
import com.hear.hear.Repositories.StudentRepository;
import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.dtos.StudentProfileDto;
import com.hear.hear.entities.Student;
import com.hear.hear.entities.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
public class StudentController {
    private final UserRepository userRepository;
    StudentRepository studentRepository;
    private final StudentProfileMapping studentProfileMapping;

    @GetMapping("getStudentProfile/{id}")
    public ResponseEntity<StudentProfileDto> getStudentProfile(@PathVariable Integer id){
        var student=  studentRepository.findById(id).orElse(null);
        if(student==null){
            return ResponseEntity.notFound().build();
        }
    return ResponseEntity.ok(studentProfileMapping.ToStudentProfileDto(student));
    }



}


















