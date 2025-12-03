package com.hear.hear.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "faculty_member")
public class FacultyMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Department")
    @Enumerated(EnumType.STRING)
    private Department department;

    @Column(name = "Job_title")
    private String jobTitle;

    @Column(name = "Scientific_degree")
    private String scientificDegree;

    @Column(name = "Bio")
    private String bio;

    @OneToOne
    @JoinColumn(name = "User_ID", referencedColumnName = "ID")
    private User user;

    @OneToMany(mappedBy = "facultyMember", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Class> classes;
}
