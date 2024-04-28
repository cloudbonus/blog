package com.github.blog.service;

import com.github.blog.dto.PostDto;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface PostService extends CrudService<PostDto, Long> {
    List<PostDto> findAllByLogin(String login);

    List<PostDto> findAllByTag(String tagName);
}
