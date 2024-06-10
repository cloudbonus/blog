package com.github.blog.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "user_info", schema = "blog")
public class UserInfo {

    @Id
    @Column(nullable = false)
    private Long id;

    @MapsId
    @OneToOne(cascade = CascadeType.MERGE, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @Column(length = Integer.MAX_VALUE)
    private String firstname;

    @Column(length = Integer.MAX_VALUE)
    private String surname;

    @Column(length = Integer.MAX_VALUE)
    private String university;

    @Column(length = Integer.MAX_VALUE)
    private String major;

    @Column(length = Integer.MAX_VALUE)
    private String company;

    @Column(length = Integer.MAX_VALUE)
    private String job;

    @Column(nullable = false)
    private String state;

    @Column(length = Integer.MAX_VALUE)
    private String stateContext;
}