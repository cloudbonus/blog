package com.github.blog.repository;

import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Post;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.PostFilter;

/**
 * @author Raman Haurylau
 */
public interface PostDao extends CrudDao<Post, Long> {
    Page<Post> findAll(PostFilter filter, Pageable pageable);
}
