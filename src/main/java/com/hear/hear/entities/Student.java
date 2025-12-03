package com.hear.hear.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
            name = "Question_Solved_By_Student",
            joinColumns = @JoinColumn(name = "Student_id"),
            inverseJoinColumns = @JoinColumn(name = "Question_id")
    )
    private List<Question> solvedQuestions;

    @ManyToMany
    @JoinTable(
            name = "Student_Enrolled_In_Course",
            joinColumns = @JoinColumn(name = "Student_id"),
            inverseJoinColumns = @JoinColumn(name = "Course_id")
    )
    private List<Course> enrolledCourses;

    @ManyToMany
    @JoinTable(
            name = "Student_Takes_Class",
            joinColumns = @JoinColumn(name = "Student_id"),
            inverseJoinColumns = @JoinColumn(name = "Class_id")
    )
    private List<Class> takesClasses;


}
