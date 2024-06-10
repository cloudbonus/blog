package com.github.blog.repository;

import com.github.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author Raman Haurylau
 */
public interface PostRepository extends CrudRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    @Override
    @NonNull
    Page<Post> findAll(@Nullable Specification<Post> spec, @NonNull Pageable pageable);
}
