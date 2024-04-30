package com.github.blog.service.mapper;

import com.github.blog.dto.common.CommentReactionDto;
import com.github.blog.model.CommentReaction;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {CommentMapper.class, UserMapper.class})
public interface CommentReactionMapper {
    CommentReaction toEntity(CommentReactionDto commentReactionDto);

    CommentReactionDto toDto(CommentReaction commentReaction);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CommentReaction partialUpdate(CommentReactionDto commentReactionDto, @MappingTarget CommentReaction commentReaction);
}