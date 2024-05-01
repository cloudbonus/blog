package com.github.blog.service;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PostDtoFilter;
import com.github.blog.controller.dto.response.Page;

/**
 * @author Raman Haurylau
 */
public interface PostService {
    Page<PostDto> findAll(PostDtoFilter requestFilter);

    PostDto create(PostDto t);

    PostDto findById(Long id);

    PostDto update(Long id, PostDto t);

    PostDto delete(Long id);
}
