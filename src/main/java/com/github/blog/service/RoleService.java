package com.github.blog.service;

import com.github.blog.controller.dto.common.RoleDto;
import com.github.blog.controller.dto.request.RoleRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.RoleFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;

/**
 * @author Raman Haurylau
 */
public interface RoleService {
    PageResponse<RoleDto> findAll(RoleFilterRequest filterRequest, PageableRequest pageableRequest);

    RoleDto create(RoleRequest t);

    RoleDto findById(Long id);

    RoleDto update(Long id, RoleRequest t);

    RoleDto delete(Long id);
}
