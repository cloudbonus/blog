package com.github.blog.repository;

import com.github.blog.model.Reaction;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;

import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface ReactionDao extends CrudDao<Reaction, Long> {
    Page<Reaction> findAll(Pageable pageable);

    Optional<Reaction> findByName(String reactionName);
}
