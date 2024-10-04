package com.github.blog.repository;

import com.github.blog.repository.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
public interface PostRepository extends CrudRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    @Override
    @NonNull
    Page<Post> findAll(@Nullable Specification<Post> spec, @NonNull Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Post p WHERE p.id IN (SELECT o.post.id FROM Order o WHERE o.state = 'CANCELED')")
    void deletePostsByCanceledOrders();
}
