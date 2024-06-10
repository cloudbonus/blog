package com.github.blog.service.mapper;

import com.github.blog.controller.dto.response.PageResponse;
import org.springframework.data.domain.Page;

/**
 * @author Raman Haurylau
 */
public interface BasePageMapper<E, DTO> {
    PageResponse<DTO> toDto(Page<E> page);
}
