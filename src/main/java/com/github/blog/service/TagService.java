package com.github.blog.service;

import com.github.blog.controller.dto.common.TagDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.TagRequest;
import com.github.blog.controller.dto.request.filter.TagDtoFilter;
import com.github.blog.controller.dto.response.Page;

/**
 * @author Raman Haurylau
 */
public interface TagService {
    Page<TagDto> findAll(TagDtoFilter filterRequest, PageableRequest pageableRequest);

    TagDto create(TagRequest t);

    TagDto findById(Long id);

    TagDto update(Long id, TagRequest t);

    TagDto delete(Long id);
}
