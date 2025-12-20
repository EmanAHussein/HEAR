package com.hear.hear.dtos;

import com.hear.hear.entities.ClassType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class ClassDto {

    private int id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;
    private String name;
    private ClassType type;
    private DayOfWeek day;
    private int courseId;
    private int facultyMemberId;
}
