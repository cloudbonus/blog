package com.github.blog.service.mapper;

import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.repository.dto.common.Page;

/**
 * @author Raman Haurylau
 */
//@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = PageableMapper.class)
public interface BasePageMapper<E, DTO> {
    PageResponse<DTO> toDto(Page<E> page);
}
