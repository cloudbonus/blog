package com.github.blog.service.mapper;

import com.github.blog.dto.PostReactionDto;
import com.github.blog.model.PostReaction;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {PostMapper.class, UserMapper.class})
public interface PostReactionMapper {
    PostReaction toEntity(PostReactionDto postReactionDto);

    PostReactionDto toDto(PostReaction postReaction);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PostReaction partialUpdate(PostReactionDto postReactionDto, @MappingTarget PostReaction postReaction);
}