package com.github.blog.repository;

import com.github.blog.model.Tag;
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
public interface TagRepository extends CrudRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    @Override
    @NonNull
    Page<Tag> findAll(@Nullable Specification<Tag> spec, @NonNull Pageable pageable);

    Optional<Tag> findByNameIgnoreCase(String name);
}
