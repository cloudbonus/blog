package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.RoleDto;
import com.github.blog.controller.dto.request.RoleRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.RoleFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Role;
import com.github.blog.repository.RoleDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.RoleFilter;
import com.github.blog.service.RoleService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    private final RoleMapper roleMapper;
    private final PageableMapper pageableMapper;

    @Override
    public RoleDto create(RoleRequest request) {
        Role role = roleMapper.toEntity(request);
        role.setRoleName("ROLE_" + role.getRoleName());
        return roleMapper.toDto(roleDao.create(role));
    }

    @Override
    public RoleDto findById(Long id) {
        Role role = roleDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ROLE_NOT_FOUND));

        return roleMapper.toDto(role);
    }

    @Override
    public PageResponse<RoleDto> findAll(RoleFilterRequest filterRequest, PageableRequest pageableRequest) {
        RoleFilter dtoFilter = roleMapper.toDto(filterRequest);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<Role> roles = roleDao.findAll(dtoFilter, pageable);

        if (roles.isEmpty()) {
            throw new CustomException(ExceptionEnum.ROLES_NOT_FOUND);
        }

        return roleMapper.toDto(roles);
    }

    @Override
    public RoleDto update(Long id, RoleRequest request) {
        Role role = roleDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ROLE_NOT_FOUND));

        role = roleMapper.partialUpdate(request, role);

        return roleMapper.toDto(role);
    }

    @Override
    public RoleDto delete(Long id) {
        Role role = roleDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ROLE_NOT_FOUND));

        roleDao.delete(role);

        return roleMapper.toDto(role);
    }
}
