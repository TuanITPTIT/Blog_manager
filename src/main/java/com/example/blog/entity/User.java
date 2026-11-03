package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // khai báo class được JPA quản lý như một Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor // sinh constructor không tham số
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // tự động sinh ID
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique=true)
    private String email;

    @Column(name="full_name")
    private String fullName;

    @Column(name="password_hash", length=255) // passwordHash la gia tri bam tu mk, kp mk that
    private String passwordHash;

    @Enumerated(EnumType.STRING) // luu gia tri USER or ADMIN vao mysql thay vi 0,1
    @Column(name="role", length=20)
    private Role role = Role.USER;

}
