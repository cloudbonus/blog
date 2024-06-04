package com.github.blog.service;

import com.github.blog.controller.dto.common.RoleDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.RoleRequest;
import com.github.blog.controller.dto.request.filter.RoleFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.dto.response.PageableResponse;
import com.github.blog.model.Role;
import com.github.blog.repository.RoleDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.RoleFilter;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.impl.RoleServiceImpl;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.RoleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Raman Haurylau
 */
@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTests {

    @Mock
    private RoleDao roleDao;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private PageableMapper pageableMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    private RoleRequest request;
    private RoleDto returnedRoleDto;
    private Role role;

    private final Long id = 1L;
    private final String roleName = "USER";

    private final Pageable pageable = new Pageable();
    private final PageableResponse pageableResponse = new PageableResponse();

    @BeforeEach
    void setUp() {
        request = new RoleRequest();
        request.setName(roleName);

        role = new Role();
        role.setId(id);
        role.setName("ROLE_" + roleName);

        returnedRoleDto = new RoleDto();
        returnedRoleDto.setId(id);
        returnedRoleDto.setName("ROLE_" + roleName);
    }

    @Test
    @DisplayName("role service: create")
    void create_returnsRoleDto_whenDataIsValid() {
        when(roleMapper.toEntity(request)).thenReturn(role);
        when(roleDao.create(role)).thenReturn(role);
        when(roleMapper.toDto(role)).thenReturn(returnedRoleDto);

        RoleDto createdRoleDto = roleService.create(request);

        assertNotNull(createdRoleDto);
        assertEquals(id, createdRoleDto.getId());
        assertEquals("ROLE_" + roleName, createdRoleDto.getName());
    }

    @Test
    @DisplayName("role service: find by id")
    void findById_returnsRoleDto_whenIdIsValid() {
        when(roleDao.findById(id)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(returnedRoleDto);

        RoleDto foundRoleDto = roleService.findById(id);

        assertNotNull(foundRoleDto);
        assertEquals(id, foundRoleDto.getId());
        assertEquals("ROLE_" + roleName, foundRoleDto.getName());
    }

    @Test
    @DisplayName("role service: find by id - not found exception")
    void findById_throwsException_whenIdIsInvalid() {
        when(roleDao.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> roleService.findById(id));

        assertEquals(ExceptionEnum.ROLE_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("role service: find all")
    void findAll_returnsPageResponse_whenDataIsValid() {
        RoleFilter filter = new RoleFilter();

        Page<Role> page = new Page<>(Collections.singletonList(role), pageable, 1L);
        PageResponse<RoleDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedRoleDto), pageableResponse, 1L);

        when(roleMapper.toEntity(any(RoleFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(roleDao.findAll(filter, pageable)).thenReturn(page);
        when(roleMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<RoleDto> foundRoles = roleService.findAll(new RoleFilterRequest(), new PageableRequest());

        assertNotNull(foundRoles);
        assertEquals(1, foundRoles.getContent().size());
        assertEquals(id, foundRoles.getContent().get(0).getId());
        assertEquals("ROLE_" + roleName, foundRoles.getContent().get(0).getName());
    }

    @Test
    @DisplayName("role service: find all - not found exception")
    void findAll_throwsException_whenNoRolesFound() {
        RoleFilter filter = new RoleFilter();

        Page<Role> page = new Page<>(Collections.emptyList(), pageable, 1L);

        when(roleMapper.toEntity(any(RoleFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(roleDao.findAll(filter, pageable)).thenReturn(page);

        CustomException exception = assertThrows(CustomException.class, () -> roleService.findAll(new RoleFilterRequest(), new PageableRequest()));

        assertEquals(ExceptionEnum.ROLES_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("role service: update")
    void update_returnsUpdatedRoleDto_whenDataIsValid() {
        when(roleDao.findById(id)).thenReturn(Optional.of(role));
        when(roleMapper.partialUpdate(request, role)).thenReturn(role);
        when(roleMapper.toDto(role)).thenReturn(returnedRoleDto);

        RoleDto updatedRoleDto = roleService.update(id, request);

        assertNotNull(updatedRoleDto);
        assertEquals(id, updatedRoleDto.getId());
        assertEquals("ROLE_" + roleName, updatedRoleDto.getName());
    }

    @Test
    @DisplayName("role service: delete")
    void delete_returnsDeletedRoleDto_whenIdIsValid() {
        when(roleDao.findById(id)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(returnedRoleDto);

        RoleDto deletedRoleDto = roleService.delete(id);

        assertNotNull(deletedRoleDto);
        assertEquals(id, deletedRoleDto.getId());
        assertEquals("ROLE_" + roleName, deletedRoleDto.getName());
        verify(roleDao, times(1)).delete(role);
    }
}