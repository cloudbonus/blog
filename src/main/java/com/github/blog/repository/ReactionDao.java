package com.github.blog.repository;

import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Reaction;
import com.github.blog.repository.dto.common.Pageable;

/**
 * @author Raman Haurylau
 */
public interface ReactionDao extends CrudDao<Reaction, Long> {
    Page<Reaction> findAll(Pageable pageable);
}
