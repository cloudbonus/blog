package com.github.blog.service.mapper;

import com.github.blog.controller.dto.common.PostDto;
import com.github.blog.controller.dto.request.PostRequest;
import com.github.blog.controller.dto.request.filter.PostFilterRequest;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
import com.github.blog.model.Tag;
import com.github.blog.repository.CommentRepository;
import com.github.blog.repository.TagRepository;
import com.github.blog.repository.filter.PostFilter;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public abstract class PostMapper implements BasePageMapper<Post, PostDto> {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Mapping(source = "tagIds", target = "tags")
    public abstract Post toEntity(PostRequest request);

    @Mapping(target = "commentIds", source = "comments")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "tagIds", source = "tags")
    public abstract PostDto toDto(Post post);

    public abstract PostFilter toEntity(PostFilterRequest requestFilter);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Post partialUpdate(PostRequest request, @MappingTarget Post post);

    public List<Tag> listToTagEntity(List<Long> ids) {
        if (ids == null) {
            return null;
        }

        List<Tag> tags = new ArrayList<>(ids.size());
        for (Long id : ids) {
            Tag tag = tagRepository
                    .findById(id)
                    .orElseThrow(() -> new CustomException(ExceptionEnum.TAG_NOT_FOUND));

            tags.add(tag);
        }

        return tags;
    }

    public List<Long> listToTagLong(List<Tag> tags) {
        if (tags == null) {
            return null;
        }

        List<Long> ids = new ArrayList<>(tags.size());
        for (Tag tag : tags) {
            ids.add(tag.getId());
        }

        return ids;
    }

    public List<Comment> listToCommentEntity(List<Long> ids) {
        if (ids == null) {
            return null;
        }

        List<Comment> comments = new ArrayList<>(ids.size());
        for (Long id : ids) {
            Comment comment = commentRepository
                    .findById(id)
                    .orElseThrow(() -> new CustomException(ExceptionEnum.COMMENT_NOT_FOUND));

            comments.add(comment);
        }

        return comments;
    }

    public List<Long> listToCommentLong(List<Comment> comments) {
        if (comments == null) {
            return null;
        }

        List<Long> ids = new ArrayList<>(comments.size());
        for (Comment comment : comments) {
            ids.add(comment.getId());
        }

        return ids;
    }
}