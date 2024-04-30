package com.github.blog.service.mapper;

import com.github.blog.dao.CommentDao;
import com.github.blog.dao.TagDao;
import com.github.blog.dto.common.PostDto;
import com.github.blog.dto.filter.PostDtoFilter;
import com.github.blog.dto.request.PostRequestFilter;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
import com.github.blog.model.Tag;
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
import java.util.Optional;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public abstract class PostMapper {
    @Autowired
    protected TagDao tagDao;
    @Autowired
    protected CommentDao commentDao;

    @Mapping(source = "commentIds", target = "comments")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "tagIds", target = "tags")
    public abstract Post toEntity(PostDto postDto);

    @Mapping(target = "commentIds", source = "comments")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "tagIds", source = "tags")
    public abstract PostDto toDto(Post post);

    public abstract PostDtoFilter toDto(PostRequestFilter requestFilter);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Post partialUpdate(PostDto postDto, @MappingTarget Post post);

    public List<Tag> listToTagEntity(List<Long> ids) {
        if (ids == null) {
            return null;
        }

        List<Tag> tags = new ArrayList<>(ids.size());
        for (Long id : ids) {
            Optional<Tag> tag = tagDao.findById(id);
            tag.ifPresent(tags::add);
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
            Optional<Comment> comment = commentDao.findById(id);
            comment.ifPresent(comments::add);
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