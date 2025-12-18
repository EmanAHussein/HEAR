package com.hear.hear.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

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
    private Integer id;

    @Column(name = "Email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "Hashed_password")
    private  String password;

    @Column(name = "Name")
    private String name;

    @Column(name = "Has_admin_permissions")
    private boolean admin;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "User_Has_Notification",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "Notification_id")
    )
    private Set<Notification> notifications;

    @ManyToMany
    @JoinTable(
            name = "Material_Favoured_By_User",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "Material_id")
    )
    private Set<Materials> favouredMaterials;

    @ManyToMany
    @JoinTable(
            name = "Question_Favoured_By_User",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "Question_id")
    )
    private List<Question> favouredQuestion;

}
