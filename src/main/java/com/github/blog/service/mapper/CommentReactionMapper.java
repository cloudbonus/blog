package com.github.blog.service.mapper;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.controller.dto.request.filter.CommentReactionFilterRequest;
import com.github.blog.model.CommentReaction;
import com.github.blog.repository.filter.CommentReactionFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CommentMapper.class, UserMapper.class})
public interface CommentReactionMapper extends BasePageMapper<CommentReaction, CommentReactionDto> {
    @Mapping(source = "commentId", target = "comment.id")
    @Mapping(source = "reactionId", target = "reaction.id")
    CommentReaction toEntity(CommentReactionRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "commentId", source = "comment.id")
    @Mapping(target = "reactionId", source = "reaction.id")
    CommentReactionDto toDto(CommentReaction commentReaction);

    CommentReactionFilter toEntity(CommentReactionFilterRequest requestFilter);
}