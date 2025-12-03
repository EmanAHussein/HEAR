package com.hear.hear.Controllers;

import com.hear.hear.Mappers.FacultyMemberProfileMapping;
import com.hear.hear.Repositories.FacultyMemRepository;
import com.hear.hear.dtos.FacultyProfileDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/faculty_member")
@AllArgsConstructor
public class FacultyMemberController {
    private FacultyMemRepository facultyMemRepository;
    private final FacultyMemberProfileMapping facultyMemberProfileMapping;

    @GetMapping("getMemberProfile/{id}")
    public ResponseEntity<FacultyProfileDto> getMemberProfile(@PathVariable Integer id){
        var facultyMember=  facultyMemRepository.findById(id).orElse(null);
        if(facultyMember==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyMemberProfileMapping.ToFacultyProfileDto(facultyMember));
    }
}
