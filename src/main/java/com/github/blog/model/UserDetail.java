package com.github.blog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class UserDetail {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "firstname", length = Integer.MAX_VALUE)
    private String firstname;

    @Column(name = "surname", length = Integer.MAX_VALUE)
    private String surname;

    @Column(name = "university_name", length = Integer.MAX_VALUE)
    private String universityName;

    @Column(name = "major_name", length = Integer.MAX_VALUE)
    private String majorName;

    @Column(name = "company_name", length = Integer.MAX_VALUE)
    private String companyName;

    @Column(name = "job_title", length = Integer.MAX_VALUE)
    private String jobTitle;
}