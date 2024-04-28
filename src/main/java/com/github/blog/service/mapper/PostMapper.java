package com.github.blog.service.mapper;

import com.github.blog.dto.PostDto;
import com.github.blog.model.Post;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface PostMapper {
    @Mapping(source = "userId", target = "user.id")
    Post toEntity(PostDto postDto);

    @Mapping(target = "userId", source = "user.id")
    PostDto toDto(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Post partialUpdate(PostDto postDto, @MappingTarget Post post);
}