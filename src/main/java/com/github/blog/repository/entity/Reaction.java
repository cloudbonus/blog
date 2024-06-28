package com.github.blog.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
@Entity
@Table(name = "reaction", schema = "blog")
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @OneToOne(mappedBy = "reaction")
    private CommentReaction commentReaction;

    @OneToOne(mappedBy = "reaction")
    private PostReaction postReactionReaction;
}
