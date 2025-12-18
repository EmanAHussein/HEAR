package com.hear.hear.dtos;

import com.hear.hear.entities.ClassType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class RegisterClassDto {
    @NotBlank(message = "start time is required")
    private LocalTime startTime;
    @NotBlank(message = "end time is required")
    private LocalTime endTime;
    @NotBlank(message = "room is required")
    private String room;
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "type is required")
    private ClassType type;
    @NotBlank(message = "day is required")
    private DayOfWeek day;
    @NotBlank(message = "course is required")
    private int courseId;
    @NotBlank(message = "faculty member is required")
    private int facultyMemberId;
}
