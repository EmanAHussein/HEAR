package com.hear.hear.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Lazy;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private LocalTime startTime;

    @Column(name = "End_time")
    private LocalTime endTime;

    @Column(name = "Room")
    private String room;

    @Column(name = "Name")
    private String name;

    @JsonIgnore
    @Column(name = "Type")
    @Enumerated(EnumType.STRING)
    private ClassType type;


    @Column(name = "Day")
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "Course_id", referencedColumnName = "ID")
    private Course course;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "Faculty_Member_id", referencedColumnName = "ID")
    @JsonBackReference
    private FacultyMember facultyMember;

    @ManyToMany(mappedBy = "takesClasses")
    @ToString.Exclude
    private Set<Student> students=new HashSet<>();


}