package com.github.blog.service;

import com.github.blog.controller.dto.common.RoleDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.RoleRequest;
import com.github.blog.controller.dto.request.filter.RoleDtoFilter;
import com.github.blog.controller.dto.response.Page;

/**
 * @author Raman Haurylau
 */
public interface RoleService {
    Page<RoleDto> findAll(RoleDtoFilter filterRequest, PageableRequest pageableRequest);

    RoleDto create(RoleRequest t);

    RoleDto findById(Long id);

    RoleDto update(Long id, RoleRequest t);

    RoleDto delete(Long id);
}
