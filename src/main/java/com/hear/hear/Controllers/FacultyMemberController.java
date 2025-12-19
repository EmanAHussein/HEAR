package com.hear.hear.Controllers;

import com.hear.hear.Mappers.FacultyProfileMapping;
import com.hear.hear.Repositories.FacultyMemRepository;
import com.hear.hear.authentication.AuthService;
import com.hear.hear.dtos.FacultyProfileDto;
import com.hear.hear.services.FacultyMemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faculty_member")
@AllArgsConstructor
public class FacultyMemberController {
    private FacultyMemRepository facultyMemRepository;
    private final FacultyProfileMapping facultyMemberProfileMapping;
    private final AuthService authService;
    private final FacultyMemberService facultyMemberService;

    @GetMapping("/profile/{id}/get")
    public ResponseEntity<FacultyProfileDto> getMemberProfile(@PathVariable Integer id){
        var facultyMember=  facultyMemRepository.findById(id).orElse(null);
        if(facultyMember==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyMemberProfileMapping.ToFacultyProfileDto(facultyMember));
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateFacultyMemberProfile(@RequestBody FacultyProfileDto facultyProfileDto) {
        var member = authService.getCurrentUser();
        var mem=facultyMemRepository.findFacultyMemberByUserId(member.getId());
        var facultyMem= facultyMemberService.updateMember(mem.get().getId(), facultyProfileDto);
        return ResponseEntity.ok(facultyMem);
    }

}
