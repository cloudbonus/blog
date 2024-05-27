package com.github.blog.service;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.PostReactionFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;

/**
 * @author Raman Haurylau
 */
public interface PostReactionService {
    PageResponse<PostReactionDto> findAll(PostReactionFilterRequest filterRequest, PageableRequest pageableRequest);

    PostReactionDto create(PostReactionRequest t);

    PostReactionDto findById(Long id);

    PostReactionDto update(Long id, PostReactionRequest t);

    PostReactionDto delete(Long id);
}
