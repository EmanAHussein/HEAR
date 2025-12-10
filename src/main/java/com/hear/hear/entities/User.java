package com.hear.hear.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Email")
    private String email;

    @Column(name = "Hashed_password")
    private  String hashedPassword;

    @Column(name = "phone")
    private String phone;


    @Column(name = "Has_admin_permissions")
    private boolean hasAdminPermissions;

    @ManyToMany
    @JoinTable(
            name = "User_Has_Notification",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "Notification_id")
    )
    private List<Notification> notifications;

    @ManyToMany
    @JoinTable(
            name = "Material_Favoured_By_User",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "Material_id")
    )
    private List<Materials> favouredMaterials;

    @ManyToMany
    @JoinTable(
            name = "Question_Favoured_By_User",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "Question_id")
    )
    private List<Question> favouredQuestion;
}
