package com.github.blog.repository;

import com.github.blog.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * @author Raman Haurylau
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    @Override
    @NonNull
    Page<Comment> findAll(@Nullable Specification<Comment> spec, @NonNull Pageable pageable);
}
