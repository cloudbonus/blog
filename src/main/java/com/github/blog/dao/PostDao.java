package com.github.blog.dao;

import com.github.blog.model.Post;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface PostDao extends CrudDao<Post, Long> {
    List<Post> findAllByLogin(String login);

    List<Post> findAllByTag(String tagName);
}
