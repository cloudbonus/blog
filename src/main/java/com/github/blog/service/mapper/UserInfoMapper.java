package com.github.blog.service.mapper;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.controller.dto.request.filter.UserInfoDtoFilter;
import com.github.blog.model.UserInfo;
import com.github.blog.repository.dto.filter.UserInfoFilter;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface UserInfoMapper {
    UserInfo toEntity(UserInfoDto request);

    UserInfoDto toDto(UserInfo userInfo);

    UserInfoFilter toDto(UserInfoDtoFilter requestFilter);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserInfo partialUpdate(UserInfoDto request, @MappingTarget UserInfo userInfo);
}