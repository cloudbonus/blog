package com.github.blog.dao;

import com.github.blog.dto.filter.CommentFilter;
import com.github.blog.model.Comment;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface CommentDao extends CrudDao<Comment, Long> {
    List<Comment> findAll(CommentFilter filter);
}
