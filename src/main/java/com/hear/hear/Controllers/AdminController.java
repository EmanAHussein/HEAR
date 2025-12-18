package com.hear.hear.Controllers;

import com.hear.hear.Repositories.FacultyMemRepository;
import com.hear.hear.Repositories.StudentRepository;
import com.hear.hear.dtos.*;
import com.hear.hear.entities.Class;
import com.hear.hear.entities.Course;
import com.hear.hear.entities.User;
import com.hear.hear.services.ClassService;
import com.hear.hear.services.CourseService;
import com.hear.hear.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;
    private final CourseService courseService;
    private final ClassService classService;
    private final StudentRepository studentRepository;
    private final FacultyMemRepository facultyMemRepository;
    //----------------------------------------------

    @GetMapping("/students/all/get")
    public ResponseEntity<?> getAllStudents() {
        var students =studentRepository.findAll();
        if(students.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/student/{studentId}/get")
    public ResponseEntity<?> getStudentById(@PathVariable Integer studentId) {
        var student =studentRepository.findById(studentId).orElse(null);
        if(student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/faculty_members/all/get")
    public ResponseEntity<?> getAllFacultyMembers() {
        var facultyMembers =facultyMemRepository.findAll();
        if(facultyMembers.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyMembers);
    }

    //    when admin click on specific member
    @GetMapping("/faculty_member/{id}/all/get'")
    public ResponseEntity<?> getMemberById(@PathVariable Integer facultyMemberId) {
        var facultyMember =studentRepository.findById(facultyMemberId).orElse(null);
        if(facultyMember == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyMember);
    }

    // assign class to student
    @PostMapping("/Student/addClass/{studentId}/{classId}")
    public ResponseEntity<?> makeStudentTakeClass(@PathVariable("studentId") Integer studentId,@PathVariable(name = "classId") Integer classId) {
        userService.enrolledStudentInClass(studentId,classId);
        return ResponseEntity.ok().build();
    }
    // assign member to class
    @PostMapping("/faculty_member/addClass/{facultyMemberId}/{classId}")
    public ResponseEntity<?> makeMemberGiveClass(@PathVariable("facultyMemberId") Integer facultyMemberId,@PathVariable(name = "classId") Integer classId) {
        userService.makeMemberGiveClass(facultyMemberId,classId);
        return ResponseEntity.ok().build();
    }

    //    this method to handel exception coming from mekeMemberGiveClass Method...
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // ----------------------------------

    @PostMapping("/user/add")
    public ResponseEntity<?> addUser(@RequestBody RegisterUserRequest registerUserRequest) {
        User user = userService.register(registerUserRequest);
        if(user == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("Duplicated User","Email or Phone is already registered"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/course/add")
    public ResponseEntity<?> addCourse(@RequestBody RegisterCourseDto registerCourseDto) {
        Course course = courseService.register(registerCourseDto);
        if(course == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("Duplicated Course","Course name or code is already registered"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    @PostMapping("/class/add")
    public ResponseEntity<?> create(@RequestBody RegisterClassDto registerClassDto) {
        Class registeredClass;
        try {
            registeredClass = classService.registerClass(registerClassDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("Error", e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredClass);
    }

    @PutMapping("/user/{id}/update")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UpdateUserDto updateUserDto) {
        Map<String, String> updatedUser;
        try {
            updatedUser = userService.updateUser(id, updateUserDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("Error", e.getMessage()));
        }
        return ResponseEntity.ok().body(updatedUser);
    }

    @PutMapping("/user/{id}/profile/update")
    public  ResponseEntity<?> updateUserProfile(@PathVariable Integer id, @RequestBody AdminUpdateUserProfile adminUpdateUserProfile) {
        Map<String, String> updatedProfile;
        try {
            updatedProfile = userService.updateUserProfile(id, adminUpdateUserProfile);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("Error", e.getMessage()));
        }
        return ResponseEntity.ok().body(updatedProfile);
    }

    @PutMapping("/course/{id}/update")
    public ResponseEntity<?> updateCourse(@PathVariable Integer id, @RequestBody UpdateCourseDto updateCourseDto) {
        Map<String, String> updatedCourse;
        try {
            updatedCourse = courseService.updateCourse(id, updateCourseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("Error", e.getMessage()));
        }
        return ResponseEntity.ok().body(updatedCourse);
    }

    @PutMapping("/class/{id}/update")
    public ResponseEntity<?> updateClass(@PathVariable Integer id, @RequestBody UpdateClassDto updateClassDto) {
        Map<String, String> updatedClass;
        try {
            updatedClass = classService.updateClass(id, updateClassDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("Error", e.getMessage()));
        }
        return ResponseEntity.ok().body(updatedClass);
    }

    @DeleteMapping("/user/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("Error", e.getMessage()));
        }
        return ResponseEntity.ok().body(Map.of("Message", "User deleted successfully"));
    }

    @DeleteMapping("/course/{id}/delete")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer id) {
        try {
            courseService.deleteCourse(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("Error", e.getMessage()));
        }
        return ResponseEntity.ok().body(Map.of("Message", "Course deleted successfully"));
    }

    @DeleteMapping("/class/{id}/delete")
    public ResponseEntity<?> deleteClass(@PathVariable Integer id) {
        try {
            classService.deleteClass(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("Error", e.getMessage()));
        }
        return ResponseEntity.ok().body(Map.of("Message", "Class deleted successfully"));
    }

}
