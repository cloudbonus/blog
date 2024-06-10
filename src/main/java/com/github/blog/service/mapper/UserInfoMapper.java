package com.github.blog.service.mapper;

import com.github.blog.controller.dto.common.UserInfoDto;
import com.github.blog.controller.dto.request.UserInfoRequest;
import com.github.blog.controller.dto.request.filter.UserInfoFilterRequest;
import com.github.blog.model.UserInfo;
import com.github.blog.repository.filter.UserInfoFilter;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface UserInfoMapper extends BasePageMapper<UserInfo, UserInfoDto> {
    UserInfo toEntity(UserInfoRequest request);

    UserInfoDto toDto(UserInfo userInfo);

    UserInfoFilter toEntity(UserInfoFilterRequest requestFilter);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserInfo partialUpdate(UserInfoRequest request, @MappingTarget UserInfo userInfo);
}