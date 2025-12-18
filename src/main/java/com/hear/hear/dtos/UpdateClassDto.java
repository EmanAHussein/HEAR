package com.hear.hear.dtos;


import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class UpdateClassDto {

    private LocalTime startTime;
    private LocalTime endTime;
    private String room;
    private DayOfWeek day;
    private int facultyMemberId;

}
