package com.hear.hear.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hear.hear.Mappers.FacultyProfileMapping;
import com.hear.hear.Mappers.StudentProfileMapping;
import com.hear.hear.Mappers.UserMapping;
import com.hear.hear.Repositories.ClassRepository;
import com.hear.hear.Repositories.FacultyMemRepository;
import com.hear.hear.Repositories.StudentRepository;
import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.dtos.*;
import com.hear.hear.entities.FacultyMember;
import com.hear.hear.entities.Role;
import com.hear.hear.entities.Student;
import com.hear.hear.entities.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepo;
    private final FacultyMemRepository facultyRepo;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserMapping userMapper;
    private final FacultyProfileMapping FacultyProfileMapper;
    private final StudentProfileMapping StudentProfileMapper;
    private final StudentProfileMapping studentProfileMapping;
    private final FacultyProfileMapping facultyProfileMapping;
    private final ClassRepository classRepository;

    //--------------------------------------------------

    public Optional<?> getUserProfile(User user){
        if(user.getRole().name().equals(Role.STUDENT.name())){
            var student = studentRepo.findStudentByUserId(user.getId()).orElse(null);
            if(student==null){
                throw new EntityNotFoundException("Student not found");
            }
            return Optional.ofNullable(studentProfileMapping.ToStudentProfileDto(student));
        }
        else if(user.getRole().name().equals(Role.FACULTYMEMBER.name())){
            var facultyMember = facultyRepo.findFacultyMemberByUserId(user.getId()).orElse(null);
            if(facultyMember==null){
                throw new EntityNotFoundException("FacultyMember not found");
            }
            return Optional.ofNullable(facultyProfileMapping.ToFacultyProfileDto(facultyMember));
        }
        return Optional.empty();
    }


    @Transactional
    public void enrolledStudentInClass(Integer studentId, Integer classId){
        var student=studentRepo.findById(studentId).orElseThrow(()->new EntityNotFoundException("Student not found"));
        var classEntity=classRepository.findById(classId).orElseThrow(()->new EntityNotFoundException("Class not found"));
        if(student.getTakesClasses().contains(classEntity)){
            throw new RuntimeException("Class is already taken");
        }
        student.getTakesClasses().add(classEntity);
        studentRepo.save(student);
    }

    @Transactional
    public void makeMemberGiveClass(Integer facultyMemberId, Integer classId){
        var facultyMember=facultyRepo.findById(facultyMemberId).orElseThrow(()->
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

    //--------------------------------------------------

    @Transactional
    public User register(RegisterUserRequest registerUserRequest) {

        if(userRepository.existsByEmail(registerUserRequest.getEmail()) || userRepository.existsByPhone(registerUserRequest.getPhone())){
            return null;
        }

        var user = userMapper.toUser(registerUserRequest);
        String hashed=passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed);

        user = userRepository.save(user);

        switch (registerUserRequest.getRole()) {
            case STUDENT -> {
                RegisterStudentProfile spDto =
                        objectMapper.convertValue(registerUserRequest.getProfile(), RegisterStudentProfile.class);

                Student sp = StudentProfileMapper.toStudent(spDto);
                sp.setUser(user);
                studentRepo.save(sp);
            }

            case FACULTYMEMBER -> {
                RegisterFacultyProfile fpDto =
                        objectMapper.convertValue(registerUserRequest.getProfile(), RegisterFacultyProfile.class);

                FacultyMember fp = FacultyProfileMapper.toFacultyMember(fpDto);
                fp.setUser(user);

                facultyRepo.save(fp);
            }
        }

        return user;
    }

    @Transactional
    public Map <String, String> updateUser(Integer id, UpdateUserDto updateUserDto) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUserFromDto(updateUserDto, user);
        if(updateUserDto.getNewPassword() != null){
            String hashed=passwordEncoder.encode(updateUserDto.getNewPassword());
            user.setPassword(hashed);
        }
        userRepository.save(user);
        return Map.of("Success", "User updated successfully");
    }


    @Transactional
    public Map <String, String> updateUserProfile(
            Integer userId,
            AdminUpdateUserProfile adminUpdateUserProfile
    ) {
        User user = userRepository.findById(userId).orElseThrow();

        Role oldRole = user.getRole();
        Role newRole = adminUpdateUserProfile.getRole();

        if (oldRole != newRole) {
            studentRepo.deleteByUser(user);
            facultyRepo.deleteByUser(user);
            studentRepo.flush();
            facultyRepo.flush();
            user.setRole(newRole);
            userRepository.save(user);
            if (newRole == Role.STUDENT) {
                Student student = studentProfileMapping.toStudent(adminUpdateUserProfile.getUpdateStudentProfile());
                student.setUser(user);
                studentRepo.save(student);
            } else {
                FacultyMember faculty = facultyProfileMapping.toFacultyMember(adminUpdateUserProfile.getUpdateFacultyProfile());
                faculty.setUser(user);
                facultyRepo.save(faculty);
            }
        } else {
            if (newRole == Role.STUDENT) {
                var student = studentRepo.findByUser(user).orElseThrow(() -> new RuntimeException("Student profile not found"));
                studentProfileMapping.updateFromDto(adminUpdateUserProfile.getUpdateStudentProfile(), (Student) student);
            } else {
                var faculty = facultyRepo.findByUser(user).orElseThrow(() -> new RuntimeException("Student profile not found"));
                facultyProfileMapping.updateFromDto(adminUpdateUserProfile.getUpdateFacultyProfile(), (FacultyMember) faculty);
            }
        }
        return Map.of("Success", "User Profile updated successfully");
    }

    @Transactional
    public void deleteUser(Integer id) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        studentRepo.deleteByUser(user);
        facultyRepo.deleteByUser(user);
        userRepository.delete(user);
    }
}
