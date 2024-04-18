package com.github.blog.dao;

import com.github.blog.model.Comment;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface CommentDao extends CrudDao<Comment, Long> {
    List<Comment> findAllByLogin(String login);
}
