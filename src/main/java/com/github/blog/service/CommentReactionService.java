package com.github.blog.service;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.controller.dto.request.CommentReactionRequest;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface CommentReactionService {
    List<CommentReactionDto> findAll();

    CommentReactionDto create(CommentReactionRequest t);

    CommentReactionDto findById(Long id);

    CommentReactionDto update(Long id, CommentReactionRequest t);

    CommentReactionDto delete(Long id);
}
