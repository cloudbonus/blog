package com.github.blog.repository;

import com.github.blog.model.CommentReaction;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.CommentReactionFilter;

import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface CommentReactionDao extends CrudDao<CommentReaction, Long> {
    Page<CommentReaction> findAll(CommentReactionFilter dtoFilter, Pageable pageable);

    Optional<CommentReaction> findByCommentIdAndUserId(Long commentId, Long userId);
}
