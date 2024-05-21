package com.github.blog.repository;

import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.PostReaction;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.PostReactionFilter;

/**
 * @author Raman Haurylau
 */
public interface PostReactionDao extends CrudDao<PostReaction, Long> {
    Page<PostReaction> findAll(PostReactionFilter dtoFilter, Pageable pageable);

}
