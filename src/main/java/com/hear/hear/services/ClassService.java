package com.hear.hear.services;


import com.hear.hear.Mappers.ClassMapping;
import com.hear.hear.Mappers.UserAllScheduleMapping;
import com.hear.hear.Repositories.ClassRepository;
import com.hear.hear.Repositories.CourseRepository;
import com.hear.hear.Repositories.FacultyMemRepository;
import com.hear.hear.Repositories.StudentRepository;
import com.hear.hear.dtos.RegisterClassDto;
import com.hear.hear.dtos.StudentScheduleDto;
import com.hear.hear.dtos.UpdateClassDto;
import com.hear.hear.entities.*;
import com.hear.hear.entities.Class;
import io.jsonwebtoken.lang.Collections;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Service
public class ClassService {

    private final FacultyMemRepository facultyRepo;
    private final CourseRepository courseRepo;
    private final ClassMapping registerClassMapping;
    private final ClassRepository classRepo;
    private final ClassMapping classMapping;
    private final StudentRepository studentRepository;
    private final UserAllScheduleMapping userAllScheduleMapping;


    public Set<StudentScheduleDto> getAllClassesByUserId(User user){
        if(user.getRole().name().equals(Role.STUDENT.name())){
            var stdClasses=studentRepository.findClassesForStudent(user.getId());
            return userAllScheduleMapping.toScheduleDto(stdClasses);
        }
        else if(user.getRole().name().equals(Role.FACULTYMEMBER.name())){
            var facClasses=facultyRepo.findClassesForFaculty(user.getId());
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
            var facClasses=facultyRepo.findClassesForFacultyMemberByDay(user.getId(),day);
            return userAllScheduleMapping.toScheduleDto(facClasses);
        }
        else return Collections.emptySet();
    }


    //--------------------------------------------------
    @Transactional
    public Class registerClass(RegisterClassDto registerClassDto) {

        if (classRepo.existsByRoom(registerClassDto.getRoom()) && classRepo.existsByDay(registerClassDto.getDay())
            && classRepo.existsByStartTimeAndEndTime(registerClassDto.getStartTime(), registerClassDto.getEndTime())) {
            throw new RuntimeException("a class already exists in the specified room and day and time");
        }
        else if (classRepo.existsByCourseAndType(courseRepo.getReferenceById(registerClassDto.getCourseId()), registerClassDto.getType()) && classRepo.existsByDay(registerClassDto.getDay())){
            throw  new RuntimeException("This class already exists in the specified course");
        }

        FacultyMember faculty = facultyRepo.findById(registerClassDto.getFacultyMemberId())
                .orElseThrow(() ->
                        new RuntimeException("Faculty with ID " + registerClassDto.getFacultyMemberId() + " not found"));

        Course course = courseRepo.findById(registerClassDto.getCourseId())
                .orElseThrow(() ->
                        new RuntimeException("Course with ID " + registerClassDto.getCourseId() + " not found"));

        Class c = registerClassMapping.toClass(registerClassDto);
        c.setFacultyMember(faculty);
        c.setCourse(course);

        return classRepo.save(c);
    }

    public Map<String, String> updateClass(Integer id, UpdateClassDto updateClassDto) {
        Class existingClass = classRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Class with ID " + id + " not found"));

        FacultyMember faculty = facultyRepo.findById(updateClassDto.getFacultyMemberId())
                .orElseThrow(() ->
                        new RuntimeException("Faculty with ID " + updateClassDto.getFacultyMemberId() + " not found"));
        classMapping.updateFromDto(updateClassDto, existingClass);
        existingClass.setFacultyMember(faculty);
        classRepo.save(existingClass);
        return Map.of("message", "Class updated successfully");
    }

    public void deleteClass(Integer id) {
        classRepo.deleteById(id);
    }
}