package com.github.blog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "\"user\"", schema = "blogging_platform")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String login;

    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String password;

    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String email;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime lastLogin;

    @ManyToMany
    @JoinTable(name = "user_role", schema = "blogging_platform", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private UserDetail userDetail;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @PrePersist
    private void prePersist(){
        createdAt = OffsetDateTime.now();
        lastLogin = OffsetDateTime.now();
    }
}