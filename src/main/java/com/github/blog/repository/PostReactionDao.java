package com.github.blog.repository;

import com.github.blog.model.PostReaction;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.PostReactionFilter;

import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface PostReactionDao extends CrudDao<PostReaction, Long> {
    Page<PostReaction> findAll(PostReactionFilter dtoFilter, Pageable pageable);
    Optional<PostReaction> findByPostIdAndUserId(Long postId, Long userId);
}
