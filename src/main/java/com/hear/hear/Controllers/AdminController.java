package com.hear.hear.Controllers;

import com.hear.hear.Repositories.FacultyMemRepository;
import com.hear.hear.Repositories.StudentRepository;
import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.dtos.StudentTakeClass;
import com.hear.hear.entities.Student;
import com.hear.hear.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final FacultyMemRepository facultyMemRepository;
    private final UserService userService;


//    we can make findAll return top 10 students to show some of the students in admin dashboard and the same on getAllFacultyMembers
    @GetMapping("/getAllStudents")
    public ResponseEntity<?> getAllStudents() {
        var students =studentRepository.findAll();
        if(students.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/getStudentById/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable Integer studentId) {
        var student =studentRepository.findById(studentId).orElse(null);
        if(student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/getAllFacultyMembers")
    public ResponseEntity<?> getAllFacultyMembers() {
        var facultyMembers =facultyMemRepository.findAll();
        if(facultyMembers.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyMembers);
    }
//    when admin click on specific member
    @GetMapping("/getMemberById/{facultyMemberId}")
    public ResponseEntity<?> getMemberById(@PathVariable Integer facultyMemberId) {
        var facultyMember =studentRepository.findById(facultyMemberId).orElse(null);
        if(facultyMember == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyMember);
    }
// assign class to student
    @PostMapping("/MakeStudentTakeClass/{studentId}/{classId}")
    public ResponseEntity<?> makeStudentTakeClass(@PathVariable("studentId") Integer studentId,@PathVariable(name = "classId") Integer classId) {
        userService.enrolledStudentInClass(studentId,classId);
        return ResponseEntity.ok().build();
    }
// assign member to class
    @PostMapping("/MakeMemberGiveClass/{facultyMemberId}/{classId}")
    public ResponseEntity<?> makeMemberGiveClass(@PathVariable("facultyMemberId") Integer facultyMemberId,@PathVariable(name = "classId") Integer classId) {
        userService.makeMemberGiveClass(facultyMemberId,classId);
        return ResponseEntity.ok().build();
    }


//    this method to handel exception coming from mekeMemberGiveClass Method...
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


//    @PutMapping
//    public ResponseEntity<?> updateStudent(@RequestBody Student student){
//
//
//    }

}
