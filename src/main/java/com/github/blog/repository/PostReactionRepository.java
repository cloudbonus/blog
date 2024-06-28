package com.github.blog.repository;

import com.github.blog.repository.entity.PostReaction;
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
public interface PostReactionRepository extends CrudRepository<PostReaction, Long>, JpaSpecificationExecutor<PostReaction> {
    @Override
    @NonNull
    Page<PostReaction> findAll(@Nullable Specification<PostReaction> spec, @NonNull Pageable pageable);

    Optional<PostReaction> findByPostIdAndUserId(Long postId, Long userId);
}
