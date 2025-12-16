package com.hear.hear.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "materials")
public class Materials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Title")
    private String title;

    @Column(name = "Type")
    private String type;

    @Column(name = "Category")
    private String category;

    @Column(name = "Link")
    private String link;

    @Column(name = "Status")
    @Enumerated(value = EnumType.STRING)
    private MQStatus status;

    @Column(name = "Time_added")
    private LocalDateTime timeAdded;

    @Column(name = "Time_approved")
    private LocalDateTime timeApproved;

    @ManyToOne
    @JoinColumn(name = "Added_by", referencedColumnName = "ID")
    private User uploader;

    @ManyToOne
    @JoinColumn(name = "Approved_by", referencedColumnName = "ID")
    private User approver;

    @ManyToOne()
    @JoinColumn(name = "Course_id", referencedColumnName = "ID")
    private Course course;

    @ManyToMany(mappedBy = "favouredMaterials")
    @JsonBackReference
    public Set<User> users=new HashSet<>();

}
