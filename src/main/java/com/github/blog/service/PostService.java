package com.github.blog.service;

import com.github.blog.dto.common.PostDto;
import com.github.blog.dto.request.PostDtoFilter;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface PostService {
    List<PostDto> findAll(PostDtoFilter requestFilter);

    PostDto create(PostDto t);

    PostDto findById(Long id);

    PostDto update(Long id, PostDto t);

    PostDto delete(Long id);
}
