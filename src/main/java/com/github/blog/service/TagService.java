package com.github.blog.service;

import com.github.blog.controller.dto.common.TagDto;
import com.github.blog.controller.dto.request.TagRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.TagFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;

/**
 * @author Raman Haurylau
 */
public interface TagService {
    PageResponse<TagDto> findAll(TagFilterRequest filterRequest, PageableRequest pageableRequest);

    TagDto create(TagRequest t);

    TagDto findById(Long id);

    TagDto update(Long id, TagRequest t);

    TagDto delete(Long id);
}
