package com.github.blog.service;

import com.github.blog.controller.dto.common.RoleDto;
import com.github.blog.controller.dto.request.RoleRequest;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface RoleService {
    List<RoleDto> findAll();

    RoleDto create(RoleRequest t);

    RoleDto findById(Long id);

    RoleDto update(Long id, RoleRequest t);

    RoleDto delete(Long id);
}
