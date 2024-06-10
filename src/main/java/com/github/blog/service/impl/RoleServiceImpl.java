package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.RoleDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.RoleRequest;
import com.github.blog.controller.dto.request.filter.RoleFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Role;
import com.github.blog.repository.RoleRepository;
import com.github.blog.repository.filter.RoleFilter;
import com.github.blog.repository.specification.RoleSpecification;
import com.github.blog.service.RoleService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public RoleDto create(RoleRequest request) {
        log.debug("Creating a new role with request: {}", request);
        Role role = roleMapper.toEntity(request);
        role.setName(ROLE_PREFIX + role.getName());

        role = roleRepository.save(role);
        log.debug("Role created successfully with ID: {}", role.getId());
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto findById(Long id) {
        log.debug("Finding role by ID: {}", id);
        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ROLE_NOT_FOUND));

        log.debug("Role found with ID: {}", id);
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RoleDto> findAll(RoleFilterRequest filterRequest, PageableRequest pageableRequest) {
        log.debug("Finding all roles with filter: {} and pageable: {}", filterRequest, pageableRequest);
        RoleFilter filter = roleMapper.toEntity(filterRequest);

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Specification<Role> spec = RoleSpecification.filterBy(filter);
        Page<Role> roles = roleRepository.findAll(spec, pageable);

        if (roles.isEmpty()) {
            throw new CustomException(ExceptionEnum.ROLES_NOT_FOUND);
        }

        log.debug("Found {} roles", roles.getTotalElements());
        return roleMapper.toDto(roles);
    }

    @Override
    public RoleDto update(Long id, RoleRequest request) {
        log.debug("Updating role with ID: {} and request: {}", id, request);
        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ROLE_NOT_FOUND));

        role = roleMapper.partialUpdate(request, role);
        log.debug("Role updated successfully with ID: {}", id);
        return roleMapper.toDto(role);
    }

    @Override
    public RoleDto delete(Long id) {
        log.debug("Deleting role with ID: {}", id);
        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.ROLE_NOT_FOUND));

        roleRepository.delete(role);
        log.debug("Role deleted successfully with ID: {}", id);
        return roleMapper.toDto(role);
    }
}