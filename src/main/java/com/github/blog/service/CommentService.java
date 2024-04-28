package com.github.blog.service;

import com.github.blog.dto.CommentDto;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface CommentService extends CrudService<CommentDto, Long> {
    List<CommentDto> findAllByLogin(String login);
}
