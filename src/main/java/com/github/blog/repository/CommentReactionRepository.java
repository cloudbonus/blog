package com.github.blog.repository;

import com.github.blog.repository.entity.CommentReaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface CommentReactionRepository extends CrudRepository<CommentReaction, Long>, JpaSpecificationExecutor<CommentReaction> {
    @Override
    @NonNull
    Page<CommentReaction> findAll(@Nullable Specification<CommentReaction> spec, @NonNull Pageable pageable);

    Optional<CommentReaction> findByCommentIdAndUserId(Long commentId, Long userId);
}
