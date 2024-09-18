package com.github.blog.repository;

import com.github.blog.repository.entity.Reaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface ReactionRepository extends CrudRepository<Reaction, Long> {
    Page<Reaction> findAll(Pageable pageable);

    Optional<Reaction> findByNameIgnoreCase(String name);
}
