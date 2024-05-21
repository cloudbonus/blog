package com.github.blog.service.mapper;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.controller.dto.request.CommentRequest;
import com.github.blog.controller.dto.request.CommentUpdateRequest;
import com.github.blog.controller.dto.request.filter.CommentDtoFilter;
import com.github.blog.model.Comment;
import com.github.blog.repository.dto.filter.CommentFilter;
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
    Comment toEntity(CommentRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "postId", source = "post.id")
    CommentDto toDto(Comment comment);

    CommentFilter toDto(CommentDtoFilter requestFilter);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment partialUpdate(CommentUpdateRequest request, @MappingTarget Comment comment);
}