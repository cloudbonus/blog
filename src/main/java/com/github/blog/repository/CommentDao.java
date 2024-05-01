package com.github.blog.repository;

import com.github.blog.repository.dto.filter.CommentFilter;
import com.github.blog.model.Comment;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface CommentDao extends CrudDao<Comment, Long> {
    List<Comment> findAll(CommentFilter filter);
}
