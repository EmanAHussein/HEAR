package com.hear.hear.Controllers;

import com.hear.hear.Mappers.ClassMapping;
import com.hear.hear.Mappers.CourseMapping;
import com.hear.hear.Mappers.FacultyProfileMapping;
import com.hear.hear.Mappers.StudentProfileMapping;
import com.hear.hear.Repositories.ClassRepository;
import com.hear.hear.Repositories.CourseRepository;
import com.hear.hear.Repositories.FacultyMemRepository;
import com.hear.hear.Repositories.StudentRepository;
import com.hear.hear.dtos.*;
import com.hear.hear.entities.Class;
import com.hear.hear.entities.Course;
import com.hear.hear.entities.User;
import com.hear.hear.services.ClassService;
import com.hear.hear.services.CourseService;
import com.hear.hear.services.UserService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
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
    private final FacultyProfileMapping facultyProfileMapping;
    private final StudentProfileMapping studentProfileMapping;
    private final CourseRepository courseRepository;
    private final CourseMapping courseMapping;
    private final ClassRepository classRepository;
    private final ClassMapping classMapping;
    //----------------------------------------------

    @ApiResponse(
            responseCode = "200",
            description = "List of user classes",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = StudentDto.class)
                    )
            )
    )
    @GetMapping("/students/all/get")
    public ResponseEntity<?> getAllStudents() {
        var students = studentProfileMapping.toDto(studentRepository.findAll());
        if(students.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @ApiResponse(
            responseCode = "200",
            description = "List of user classes",
            content = @Content(
                    mediaType = "application/json",
                            schema = @Schema(implementation = StudentDto.class)
            )
    )
    @GetMapping("/students/{studentId}/get")
    public ResponseEntity<?> getStudentById(@PathVariable Integer studentId) {
        var student = studentProfileMapping.toDto(studentRepository.findById(studentId).orElse(null));
        if(student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @ApiResponse(
            responseCode = "200",
            description = "List of user classes",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = FacultyMemberDto.class)
                    )
            )
    )
    @GetMapping("/faculty_members/all/get")
    public ResponseEntity<?> getAllFacultyMembers() {
        var facultyMembers = facultyProfileMapping.toDto(facultyMemRepository.findAll());
        if(facultyMembers.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyMembers);
    }

    @ApiResponse(
            responseCode = "200",
            description = "List of user classes",
            content = @Content(
                    mediaType = "application/json",
                            schema = @Schema(implementation = FacultyMemberDto.class)
            )
    )
    //    when admin click on specific member
    @GetMapping("/faculty_members/{facultyMemberId}/get'")
    public ResponseEntity<?> getMemberById(@PathVariable Integer facultyMemberId) {
        var facultyMember = facultyProfileMapping.toDto(facultyMemRepository.findById(facultyMemberId).orElse(null));
        if(facultyMember == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyMember);
    }

    @GetMapping("/courses/all/get")
    public ResponseEntity<?> getAllCourses() {
        var courses = courseMapping.toDto(courseRepository.findAll());
        if(courses.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/classes/all/get")
    public ResponseEntity<?> getAllClasses() {
        var classes = classMapping.toDto(classRepository.findAll());
        if(classes.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(classes);
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

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {

                            @ExampleObject(
                                    name = "Student Register",
                                    summary = "Register a student",
                                    value = """
                {
                  "email": "student@example.com",
                  "password": "string",
                  "name": "Student Name",
                  "phone": "0100000000",
                  "role": "STUDENT",
                  "profile": {
                    "studentCode": 123456,
                    "currentLevel": 3,
                    "department": "CS"
                  },
                  "admin": false
                }
                """
                            ),

                            @ExampleObject(
                                    name = "Faculty Register",
                                    summary = "Register a faculty member",
                                    value = """
                {
                  "email": "faculty@example.com",
                  "password": "string",
                  "name": "Faculty Name",
                  "phone": "0100000000",
                  "role": "FACULTYMEMBER",
                  "profile": {
                    "jobTitle": "Professor",
                    "department": "CS",
                    "scientificDegree": "PhD",
                    "bio": "Some bio"
                  },
                  "admin": false
                }
                """
                            )
                    }
            )
    )

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
    public ResponseEntity<?> addCourse(@RequestBody RegisterCourseDto registerCourseDto) throws BadRequestException {
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

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", ex.getMessage()));
    }

}
