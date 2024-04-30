package com.github.blog.dao;

import com.github.blog.dto.filter.PostDtoFilter;
import com.github.blog.model.Post;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface PostDao extends CrudDao<Post, Long> {
    List<Post> findAll(PostDtoFilter filter);
}
