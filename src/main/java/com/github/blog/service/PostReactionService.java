package com.github.blog.service;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.controller.dto.request.filter.PostReactionDtoFilter;
import com.github.blog.controller.dto.response.Page;

/**
 * @author Raman Haurylau
 */
public interface PostReactionService {
    Page<PostReactionDto> findAll(PostReactionDtoFilter filterRequest, PageableRequest pageableRequest);

    PostReactionDto create(PostReactionRequest t);

    PostReactionDto findById(Long id);

    PostReactionDto update(Long id, PostReactionRequest t);

    PostReactionDto delete(Long id);
}
