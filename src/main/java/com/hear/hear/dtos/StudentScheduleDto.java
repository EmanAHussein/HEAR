package com.hear.hear.dtos;

import com.hear.hear.entities.ClassType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;
import java.time.DayOfWeek;

@Getter
@AllArgsConstructor
public class StudentScheduleDto {
    private Time startTime;
    private Time endTime;
    private String room;
    private String name;
    private String type;
    private String day;
}