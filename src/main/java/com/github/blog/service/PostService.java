package com.github.blog.service;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostRequest;
import com.github.blog.controller.dto.request.filter.PostDtoFilter;
import com.github.blog.controller.dto.response.Page;

/**
 * @author Raman Haurylau
 */
public interface PostService {
    Page<PostDto> findAll(PostDtoFilter requestFilter, PageableRequest pageableRequest);

    PostDto create(PostRequest t);

    PostDto findById(Long id);

    PostDto update(Long id, PostRequest t);

    PostDto delete(Long id);
}
