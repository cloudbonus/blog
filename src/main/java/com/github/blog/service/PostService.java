package com.github.blog.service;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PostRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.PostFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;

/**
 * @author Raman Haurylau
 */
public interface PostService {
    PageResponse<PostDto> findAll(PostFilterRequest requestFilter, PageableRequest pageableRequest);

    PostDto create(PostRequest t);

    PostDto findById(Long id);

    PostDto update(Long id, PostRequest t);

    PostDto delete(Long id);
}
