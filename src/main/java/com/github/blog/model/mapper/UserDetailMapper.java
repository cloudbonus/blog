package com.github.blog.model.mapper;

import com.github.blog.dto.UserDetailDto;
import com.github.blog.model.UserDetail;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface UserDetailMapper {
    UserDetail toEntity(UserDetailDto userDetailDto);

    UserDetailDto toDto(UserDetail userDetail);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserDetail partialUpdate(UserDetailDto userDetailDto, @MappingTarget UserDetail userDetail);
}