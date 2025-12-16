package com.hear.hear.services;

import com.hear.hear.Mappers.FacultyMemberMapping;
import com.hear.hear.Mappers.StudentMapping;
import com.hear.hear.Mappers.UserAllScheduleMapping;
import com.hear.hear.Repositories.ClassRepository;
import com.hear.hear.Repositories.FacultyMemRepository;
import com.hear.hear.Repositories.StudentRepository;
import com.hear.hear.dtos.StudentProfileDto;
import com.hear.hear.dtos.StudentScheduleDto;
import com.hear.hear.entities.Role;
import com.hear.hear.entities.User;
import io.jsonwebtoken.lang.Collections;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final FacultyMemRepository facultyMemRepository;
    private StudentMapping studentMapping;
    private FacultyMemberMapping facultyMemberMapping;


    public Optional<?> getUserProfile(User user){
        if(user.getRole().name().equals(Role.STUDENT.name())){
            var student = studentRepository.findStudentByUserId(user.getId()).orElse(null);
            if(student==null){
                throw new EntityNotFoundException("Student not found");
            }
            return Optional.ofNullable(studentMapping.ToStudentProfileDto(student));
        }
        else if(user.getRole().name().equals(Role.FACULTYMEMBER.name())){
            var facultyMember = facultyMemRepository.findFacultyMemberByUserId(user.getId()).orElse(null);
            if(facultyMember==null){
                throw new EntityNotFoundException("FacultyMember not found");
            }
            return Optional.ofNullable(facultyMemberMapping.ToFacultyProfileDto(facultyMember));
        }
        return Optional.empty();
    }


    @Transactional
    public void enrolledStudentInClass(Integer studentId, Integer classId){
    var student=studentRepository.findById(studentId).orElseThrow(()->new EntityNotFoundException("Student not found"));
    var classEntity=classRepository.findById(classId).orElseThrow(()->new EntityNotFoundException("Class not found"));
    if(student.getTakesClasses().contains(classEntity)){
        throw new RuntimeException("Class is already taken");
    }
    student.getTakesClasses().add(classEntity);
    studentRepository.save(student);
}

    @Transactional
    public void makeMemberGiveClass(Integer facultyMemberId, Integer classId){
        var facultyMember=facultyMemRepository.findById(facultyMemberId).orElseThrow(()->
                new RuntimeException("Member Not Found"));
        var classEntity=classRepository.findById(classId).orElseThrow(()->
                new RuntimeException("Class Not Found"));

        if(classEntity.getFacultyMember()!=null &&
                Integer.valueOf(classEntity.getFacultyMember().getId()).equals(facultyMemberId)){
        throw new RuntimeException("Class is already taken By This Member");
        }
        if(classEntity.getFacultyMember()!=null){
            throw new RuntimeException("Class is already taken By Another Member");
        }
        classEntity.setFacultyMember(facultyMember);
        facultyMember.getClasses().add(classEntity);
        classRepository.save(classEntity);
    }





}
