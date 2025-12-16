package com.hear.hear.services;

import com.hear.hear.Mappers.FacultyMemberMapping;
import com.hear.hear.Repositories.FacultyMemRepository;
import com.hear.hear.dtos.FacultyProfileDto;
import com.hear.hear.entities.Department;
import com.hear.hear.entities.FacultyMember;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@AllArgsConstructor
@Service
public class FacultyMemberService {

    private final FacultyMemRepository facultyMemRepository;
    private final FacultyMemberMapping facultyMemberMapping;

    @Transactional
    public FacultyProfileDto updateMember(Integer Id, FacultyProfileDto request){
        var facultyMember=facultyMemRepository.findById(Id).orElseThrow
                (()->new RuntimeException("Faculty Member Not Found"));
        facultyMember.setBio(request.getBio());
        facultyMember.setJobTitle(request.getJobTitle());
        facultyMember.setScientificDegree(request.getScientificDegree());
        Department department;
        try {
             department=Department.valueOf(request.getDepartment().name().toUpperCase());
        }catch (IllegalArgumentException e){throw new RuntimeException("Invalid Department..");}
        facultyMember.setDepartment(department);

        facultyMemRepository.save(facultyMember);
        return facultyMemberMapping.ToFacultyProfileDto(facultyMember);
    }

}
