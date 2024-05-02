package com.github.blog.service.mapper;

import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.repository.dto.common.Pageable;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Raman Haurylau
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PageableMapper {
    Pageable toDto(PageableRequest pageableRequest);
}
