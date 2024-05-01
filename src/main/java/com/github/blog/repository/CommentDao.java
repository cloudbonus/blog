package com.github.blog.repository;

import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Comment;
import com.github.blog.repository.dto.filter.CommentFilter;

/**
 * @author Raman Haurylau
 */
public interface CommentDao extends CrudDao<Comment, Long> {
    Page<Comment> findAll(CommentFilter filter);
}
