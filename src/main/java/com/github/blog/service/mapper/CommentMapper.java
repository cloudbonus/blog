package com.github.blog.service.mapper;

import com.github.blog.dto.common.CommentDto;
import com.github.blog.dto.filter.CommentDtoFilter;
import com.github.blog.dto.request.CommentRequestFilter;
import com.github.blog.model.Comment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {PostMapper.class, UserMapper.class})
public interface CommentMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "postId", target = "post.id")
    Comment toEntity(CommentDto commentDto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "postId", source = "post.id")
    CommentDto toDto(Comment comment);

    CommentDtoFilter toDto(CommentRequestFilter requestFilter);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment partialUpdate(CommentDto commentDto, @MappingTarget Comment comment);
}