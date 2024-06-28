package com.github.blog.service.mapper;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.controller.dto.request.PostReactionRequest;
import com.github.blog.controller.dto.request.filter.PostReactionFilterRequest;
import com.github.blog.repository.entity.PostReaction;
import com.github.blog.repository.filter.PostReactionFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {PostMapper.class, UserMapper.class})
public interface PostReactionMapper extends BasePageMapper<PostReaction, PostReactionDto> {
    @Mapping(source = "postId", target = "post.id")
    @Mapping(source = "reactionId", target = "reaction.id")
    PostReaction toEntity(PostReactionRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "reactionId", source = "reaction.id")
    PostReactionDto toDto(PostReaction postReaction);

    PostReactionFilter toEntity(PostReactionFilterRequest requestFilter);
}