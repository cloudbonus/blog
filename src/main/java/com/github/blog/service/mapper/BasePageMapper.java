package com.github.blog.service.mapper;

import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.repository.dto.common.Page;

/**
 * @author Raman Haurylau
 */
public interface BasePageMapper<E, DTO> {
    PageResponse<DTO> toDto(Page<E> page);
}
