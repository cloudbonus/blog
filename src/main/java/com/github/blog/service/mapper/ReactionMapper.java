package com.github.blog.service.mapper;

import com.github.blog.controller.dto.common.ReactionDto;
import com.github.blog.controller.dto.request.ReactionRequest;
import com.github.blog.repository.entity.Reaction;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * @author Raman Haurylau
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReactionMapper extends BasePageMapper<Reaction, ReactionDto> {
    Reaction toEntity(ReactionRequest request);

    ReactionDto toDto(Reaction reaction);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Reaction partialUpdate(ReactionRequest request, @MappingTarget Reaction reaction);
}
