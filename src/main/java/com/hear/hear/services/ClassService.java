package com.hear.hear.services;

import com.hear.hear.Mappers.UserAllScheduleMapping;
import com.hear.hear.Repositories.FacultyMemRepository;
import com.hear.hear.Repositories.StudentRepository;
import com.hear.hear.dtos.StudentScheduleDto;
import com.hear.hear.entities.Role;
import com.hear.hear.entities.User;
import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Set;
@AllArgsConstructor
@Service
public class ClassService {
    private final AuthService authService;
    private final StudentRepository studentRepository;
    private final FacultyMemRepository facultyMemRepository;
    private final UserAllScheduleMapping userAllScheduleMapping;

    public Set<StudentScheduleDto> getAllClassesByUserId(User user){
        if(user.getRole().name().equals(Role.STUDENT.name())){
            var stdClasses=studentRepository.findClassesForStudent(user.getId());
           return userAllScheduleMapping.toScheduleDto(stdClasses);
        }
        else if(user.getRole().name().equals(Role.FACULTYMEMBER.name())){
                var facClasses=facultyMemRepository.findClassesForFaculty(user.getId());
            return userAllScheduleMapping.toScheduleDto(facClasses);
        }
       else return Collections.emptySet();
    }


    public Set<StudentScheduleDto> getAllClassesByUserIdAndDay(User user, DayOfWeek day){
        if(user.getRole().name().equals(Role.STUDENT.name())){
            var stdClasses=studentRepository.findClassesForStudentByDay(user.getId(),day);
            return userAllScheduleMapping.toScheduleDto(stdClasses);
        }
        else if(user.getRole().name().equals(Role.FACULTYMEMBER.name())){
            var facClasses=facultyMemRepository.findClassesForFacultyMemberByDay(user.getId(),day);
            return userAllScheduleMapping.toScheduleDto(facClasses);
        }
        else return Collections.emptySet();
    }

}
