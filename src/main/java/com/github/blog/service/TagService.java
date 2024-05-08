package com.github.blog.service;

import com.github.blog.controller.dto.common.TagDto;
import com.github.blog.controller.dto.request.TagRequest;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface TagService {
    List<TagDto> findAll();

    TagDto create(TagRequest t);

    TagDto findById(Long id);

    TagDto update(Long id, TagRequest t);

    TagDto delete(Long id);
}
