package com.github.blog.service.mapper;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.UserDtoFilter;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.model.User;
import com.github.blog.repository.dto.filter.UserFilter;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserRequest request);

    UserDto toDto(User user);

    UserFilter toDto(UserDtoFilter requestFilter);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserRequest request, @MappingTarget User user);

}