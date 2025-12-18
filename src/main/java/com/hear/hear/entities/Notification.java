package com.hear.hear.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Title")
    private String title;

    @Column(name = "Message")
    private String message;

    @Column(name = "Created_At")
    private LocalDateTime createdAt;
//----------------------------------------
    @ManyToOne
    @JoinColumn(name = "Creator_ID", referencedColumnName = "ID")
    private User creator;

//    @ManyToMany(mappedBy = "notifications")
//    @JsonBackReference
//    private Set<User> creator;

}
//---------------------------------------------
