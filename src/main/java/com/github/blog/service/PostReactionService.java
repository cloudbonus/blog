package com.github.blog.service;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PostReactionRequest;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface PostReactionService {
    List<PostReactionDto> findAll();

    PostReactionDto create(PostReactionRequest t);

    PostReactionDto findById(Long id);

    PostReactionDto update(Long id, PostReactionRequest t);

    PostReactionDto delete(Long id);
}
