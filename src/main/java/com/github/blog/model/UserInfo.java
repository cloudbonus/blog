package com.github.blog.model;

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
@Table(name = "user_details", schema = "blogging_platform")
public class UserInfo {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = Integer.MAX_VALUE)
    private String firstname;

    @Column(length = Integer.MAX_VALUE)
    private String surname;

    @Column(length = Integer.MAX_VALUE)
    private String universityName;

    @Column(length = Integer.MAX_VALUE)
    private String majorName;

    @Column(length = Integer.MAX_VALUE)
    private String companyName;

    @Column(length = Integer.MAX_VALUE)
    private String jobTitle;

    @Column(nullable = false)
    private String state;

    @Column(length = Integer.MAX_VALUE)
    private String stateContext;
}