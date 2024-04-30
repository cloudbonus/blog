package com.github.blog.service.impl;

import com.github.blog.dao.RoleDao;
import com.github.blog.dto.common.RoleDto;
import com.github.blog.model.Role;
import com.github.blog.service.RoleService;
import com.github.blog.service.exception.RoleErrorResult;
import com.github.blog.service.exception.impl.RoleException;
import com.github.blog.service.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    private final RoleMapper roleMapper;

    @Override
    public RoleDto create(RoleDto roleDto) {
        Role role = roleMapper.toEntity(roleDto);
        return roleMapper.toDto(roleDao.create(role));
    }

    @Override
    public RoleDto findById(Long id) {
        Role role = roleDao
                .findById(id)
                .orElseThrow(() -> new RoleException(RoleErrorResult.ROLE_NOT_FOUND));

        return roleMapper.toDto(role);
    }

    @Override
    public List<RoleDto> findAll() {
        List<Role> roles = roleDao.findAll();

        if (roles.isEmpty()) {
            throw new RoleException(RoleErrorResult.ROLES_NOT_FOUND);
        }

        return roles.stream().map(roleMapper::toDto).toList();
    }

    @Override
    public RoleDto update(Long id, RoleDto roleDto) {
        Role role = roleDao
                .findById(id)
                .orElseThrow(() -> new RoleException(RoleErrorResult.ROLE_NOT_FOUND));

        role = roleMapper.partialUpdate(roleDto, role);
        role = roleDao.update(role);

        return roleMapper.toDto(role);
    }

    @Override
    public RoleDto delete(Long id) {
        Role role = roleDao
                .findById(id)
                .orElseThrow(() -> new RoleException(RoleErrorResult.ROLE_NOT_FOUND));
        roleDao.delete(role);
        return roleMapper.toDto(role);
    }
}
