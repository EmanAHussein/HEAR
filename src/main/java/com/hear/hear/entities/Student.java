package com.hear.hear.entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student")
@ToString
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Student_code")
    private int studentCode;

    @Column(name = "Current_level")
    private byte currentLevel;

    @Column(name = "Department")
    @Enumerated(EnumType.STRING)
    private Department department;

    @OneToOne
    @JoinColumn(name = "User_ID", referencedColumnName = "ID")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "student_enrolled_in_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonManagedReference
    private Set<Course> enrolledCourses=new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "student_takes_class",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    @JsonManagedReference
    @ToString.Exclude
    private Set<Class> takesClasses=new HashSet<>();

}
