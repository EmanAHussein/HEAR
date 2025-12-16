package com.hear.hear.entities;

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
    private boolean hasAdminPermissions;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private Role role;

    @ManyToMany
    @JsonManagedReference
    @JoinTable(
            name = "user_has_notification",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "Notification_id")
    )
    private Set<Notification> notifications;

    @ManyToMany
    @JoinTable(
            name = "material_favoured_by_user",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "Material_id")
    )
    private Set<Materials> favouredMaterials=new HashSet<>();

}
