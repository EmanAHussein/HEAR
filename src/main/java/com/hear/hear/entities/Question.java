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
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Question_text")
    private String questionText;

    @Column(name = "Correct_answer")
    private String correctAnswer;

    @Column(name = "Status")
    private MQStatus status;

    @Column(name = "Time_added")
    private LocalDateTime timeAdded;

    @Column(name = "Time_approved")
    private LocalDateTime timeApproved;

    @Column(name = "Type")
    private QType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Choices", joinColumns = @JoinColumn(name = "Question_id"))
    @Column(name = "Choice")
    private java.util.List<String> choices;

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
