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
    private static final String ROLE_PREFIX = "ROLE_";

    private final Pageable pageable = new Pageable();
    private final PageableRequest pageableRequest = new PageableRequest(null, null, null);
    private final RoleFilterRequest roleFilterRequest = new RoleFilterRequest(null);
    private final PageableResponse pageableResponse = new PageableResponse(0, 0, null);

    @BeforeEach
    void setUp() {
        request = new RoleRequest(roleName);

        role = new Role();
        role.setId(id);
        role.setName(ROLE_PREFIX + roleName);

        returnedRoleDto = new RoleDto(id, ROLE_PREFIX + roleName);
    }

    @Test
    @DisplayName("role service: create")
    void create_returnsRoleDto_whenDataIsValid() {
        when(roleMapper.toEntity(request)).thenReturn(role);
        when(roleDao.create(role)).thenReturn(role);
        when(roleMapper.toDto(role)).thenReturn(returnedRoleDto);

        RoleDto createdRoleDto = roleService.create(request);

        assertNotNull(createdRoleDto);
        assertEquals(id, createdRoleDto.id());
        assertEquals("ROLE_" + roleName, createdRoleDto.name());
    }

    @Test
    @DisplayName("role service: find by id")
    void findById_returnsRoleDto_whenIdIsValid() {
        when(roleDao.findById(id)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(returnedRoleDto);

        RoleDto foundRoleDto = roleService.findById(id);

        assertNotNull(foundRoleDto);
        assertEquals(id, foundRoleDto.id());
        assertEquals(ROLE_PREFIX + roleName, foundRoleDto.name());
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

        PageResponse<RoleDto> foundRoles = roleService.findAll(roleFilterRequest, pageableRequest);

        assertNotNull(foundRoles);
        assertEquals(1, foundRoles.content().size());
        assertEquals(id, foundRoles.content().get(0).id());
        assertEquals(ROLE_PREFIX + roleName, foundRoles.content().get(0).name());
    }

    @Test
    @DisplayName("role service: find all - not found exception")
    void findAll_throwsException_whenNoRolesFound() {
        RoleFilter filter = new RoleFilter();

        Page<Role> page = new Page<>(Collections.emptyList(), pageable, 1L);

        when(roleMapper.toEntity(any(RoleFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(roleDao.findAll(filter, pageable)).thenReturn(page);

        CustomException exception = assertThrows(CustomException.class, () -> roleService.findAll(roleFilterRequest, pageableRequest));

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
        assertEquals(id, updatedRoleDto.id());
        assertEquals(ROLE_PREFIX + roleName, updatedRoleDto.name());
    }

    @Test
    @DisplayName("role service: delete")
    void delete_returnsDeletedRoleDto_whenIdIsValid() {
        when(roleDao.findById(id)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(returnedRoleDto);

        RoleDto deletedRoleDto = roleService.delete(id);

        assertNotNull(deletedRoleDto);
        assertEquals(id, deletedRoleDto.id());
        assertEquals(ROLE_PREFIX + roleName, deletedRoleDto.name());
        verify(roleDao, times(1)).delete(role);
    }
}