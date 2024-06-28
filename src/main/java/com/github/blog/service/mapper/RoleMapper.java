package com.github.blog.service.mapper;

import com.github.blog.controller.dto.common.RoleDto;
import com.github.blog.controller.dto.request.RoleRequest;
import com.github.blog.controller.dto.request.filter.RoleFilterRequest;
import com.github.blog.repository.entity.Role;
import com.github.blog.repository.filter.RoleFilter;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface RoleMapper extends BasePageMapper<Role, RoleDto> {
    Role toEntity(RoleRequest request);

    RoleDto toDto(Role role);

    RoleFilter toEntity(RoleFilterRequest requestFilter);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Role partialUpdate(RoleRequest request, @MappingTarget Role role);
}