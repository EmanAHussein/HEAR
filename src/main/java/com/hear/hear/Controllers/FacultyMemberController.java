package com.hear.hear.Controllers;

import com.hear.hear.Mappers.FacultyMemberMapping;
import com.hear.hear.Repositories.FacultyMemRepository;
import com.hear.hear.dtos.FacultyProfileDto;
import com.hear.hear.entities.Course;
import com.hear.hear.services.AuthService;
import com.hear.hear.services.FacultyMemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/faculty_member")
@AllArgsConstructor
public class FacultyMemberController {
    private final FacultyMemRepository facultyMemRepository;
    private final FacultyMemberService facultyMemberService;
    private final AuthService authService;
    private final FacultyMemberMapping facultyMemberMapping;


    @PutMapping("/updateProfileData")
    public ResponseEntity<?> updateFacultyMemberProfile(@RequestBody FacultyProfileDto facultyProfileDto) {
    var member = authService.getCurrentUser();
    var mem=facultyMemRepository.findFacultyMemberByUserId(member.getId());
   var facultyMem= facultyMemberService.updateMember(mem.get().getId(), facultyProfileDto);
    return ResponseEntity.ok(facultyMem);
}


}
