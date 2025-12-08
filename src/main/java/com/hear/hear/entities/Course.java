package com.hear.hear.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Name")
    private String Name;

    @Column(name = "Course_code")
    private String code;

    @Column(name = "Credit_hours")
    private byte creditHours;

    @Column(name = "Description")
    private String description;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Class> classes;

    @ManyToMany(mappedBy = "enrolledCourses")
    @ToString.Exclude
    @JsonBackReference
    private Set<Student> students=new HashSet<>();

}
