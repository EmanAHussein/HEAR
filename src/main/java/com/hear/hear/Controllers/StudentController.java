package com.hear.hear.Controllers;

import com.hear.hear.Mappers.StudentAllScheduleMapping;
import com.hear.hear.Mappers.StudentProfileMapping;
import com.hear.hear.Repositories.ClassRepository;
import com.hear.hear.Repositories.StudentRepository;
import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.dtos.StudentProfileDto;
import com.hear.hear.dtos.StudentScheduleDto;
import com.hear.hear.entities.Course;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.Set;
import com.hear.hear.entities.Class;
@RestController
@RequestMapping("/student")
@AllArgsConstructor
public class StudentController {
    private final UserRepository userRepository;
    private final ClassRepository classRepository;
    StudentRepository studentRepository;
    private final StudentProfileMapping studentProfileMapping;
    private final StudentAllScheduleMapping studentAllScheduleMapping;


    @GetMapping("getStudentProfile/{id}")
    public ResponseEntity<StudentProfileDto> getStudentProfile(@PathVariable Integer id){
        var student=  studentRepository.findById(id).orElse(null);
        if(student==null){
            return ResponseEntity.notFound().build();
        }
    return ResponseEntity.ok(studentProfileMapping.ToStudentProfileDto(student));
    }


@GetMapping("gitSchedual/{studentId}")
public ResponseEntity<Set<StudentScheduleDto>>getClassesByUserid(@PathVariable Integer studentId){
    var classes=  studentRepository.findClassesForStudent(studentId);
    if(classes==null){
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(studentAllScheduleMapping.toScheduleDto(classes));
}

    @GetMapping("gitSchedual/{studentId}/{day}")
    public ResponseEntity<Set<StudentScheduleDto>>getClassesByUserid(@PathVariable Integer studentId,@PathVariable DayOfWeek day){
        var classes=  studentRepository.findClassesForStudentByDay(studentId,day);
        if(classes==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentAllScheduleMapping.toScheduleDto(classes));
    }

    @GetMapping("gitCourses/{studentId}")
    public ResponseEntity<Set<Course>> getCourses(@PathVariable Integer studentId){
        var courses=studentRepository.findCoursesForStudentByStudentId(studentId);
        if(courses==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(courses);
    }



}


















