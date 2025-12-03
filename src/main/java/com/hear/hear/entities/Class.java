package com.hear.hear.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Time;
import java.time.DayOfWeek;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "class")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Start_time")
    private Time startTime;

    @Column(name = "End_time")
    private Time endTime;

    @Column(name = "Room")
    private String room;

    @Column(name = "Name")
    private String name;

    @Column(name = "Type")
    private ClassType type;

    @Column(name = "Day")
    private DayOfWeek day;

    @ManyToOne
    @JoinColumn(name = "Course_id", referencedColumnName = "ID")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "Faculty_Member_id", referencedColumnName = "ID")
    private FacultyMember facultyMember;
}
