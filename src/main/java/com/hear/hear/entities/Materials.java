package com.hear.hear.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
}
