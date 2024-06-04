package com.github.blog.service;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.request.filter.UserFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.dto.response.PageableResponse;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.repository.RoleDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.UserFilter;
import com.github.blog.service.impl.UserServiceImpl;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Raman Haurylau
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserDao userDao;

    @Mock
    private RoleDao roleDao;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PageableMapper pageableMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto returnedUserDto;
    private final User user = new User();
    private final UserRequest request = new UserRequest();

    private final Long id = 1L;
    private final String login = "test login";

    private final Pageable pageable = new Pageable();
    private final PageableResponse pageableResponse = new PageableResponse();

    @BeforeEach
    void setUp() {
        returnedUserDto = new UserDto();
        returnedUserDto.setId(id);
        returnedUserDto.setUsername(login);
    }

    @Test
    @DisplayName("user service: create")
    void create_returnsUserDto_whenDataIsValid() {
        when(userMapper.toEntity(request)).thenReturn(user);
        when(roleDao.findByName("ROLE_USER")).thenReturn(Optional.of(new Role()));
        when(userDao.create(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        UserDto createdUserDto = userService.create(request);

        assertThat(createdUserDto).isNotNull();
        assertThat(createdUserDto.getId()).isEqualTo(id);
        assertThat(createdUserDto.getUsername()).isEqualTo(login);
    }

    @Test
    @DisplayName("user service: update")
    void update_returnsUpdatedUserDto_whenDataIsValid() {
        when(userDao.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.partialUpdate(request, user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);
        UserDto updatedUserDto = userService.update(id, request);

        assertThat(updatedUserDto).isNotNull();
        assertThat(updatedUserDto.getId()).isEqualTo(id);
        assertThat(updatedUserDto.getUsername()).isEqualTo(login);
    }

    @Test
    @DisplayName("user service: delete")
    void delete_deletesUser_whenDataIsValid() {
        when(userDao.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        UserDto deletedUserDto = userService.delete(id);

        assertThat(deletedUserDto).isNotNull();
        assertThat(deletedUserDto.getId()).isEqualTo(id);
        assertThat(deletedUserDto.getUsername()).isEqualTo(login);
        verify(userDao, times(1)).delete(user);
    }

    @Test
    @DisplayName("user service: find all by role")
    void find_findsAllUsersByRole_whenDataIsValid() {
        UserFilter filter = new UserFilter();

        Page<User> page = new Page<>(List.of(user), pageable, 1L);
        PageResponse<UserDto> pageResponse = new PageResponse<>(List.of(returnedUserDto), pageableResponse, 1L);

        when(userMapper.toEntity(any(UserFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(userDao.findAll(filter, pageable)).thenReturn(page);
        when(userMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<UserDto> filterSearchResult = userService.findAll(new UserFilterRequest(), new PageableRequest());

        assertThat(filterSearchResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getUsername).containsExactly(login);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getId).containsExactly(id);
    }

    @Test
    @DisplayName("user: find by username")
    void find_findsUserByUsername_whenDataIsValid() {
        UserFilter filter = new UserFilter();

        Page<User> page = new Page<>(List.of(user), pageable, 1L);
        PageResponse<UserDto> pageResponse = new PageResponse<>(List.of(returnedUserDto), pageableResponse, 1L);

        when(userMapper.toEntity(any(UserFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(userDao.findAll(filter, pageable)).thenReturn(page);
        when(userMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<UserDto> filterSearchResult = userService.findAll(new UserFilterRequest(), new PageableRequest());

        assertThat(filterSearchResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getUsername).containsExactly(login);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getId).containsExactly(id);
    }

    @Test
    @DisplayName("user service: find all by job")
    void find_findsAllUsersByJob_whenDataIsValid() {
        UserFilter filter = new UserFilter();

        Page<User> page = new Page<>(List.of(user), pageable, 1L);
        PageResponse<UserDto> pageResponse = new PageResponse<>(List.of(returnedUserDto), pageableResponse, 1L);

        when(userMapper.toEntity(any(UserFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(userDao.findAll(filter, pageable)).thenReturn(page);
        when(userMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<UserDto> filterSearchResult = userService.findAll(new UserFilterRequest(), new PageableRequest());

        assertThat(filterSearchResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getUsername).containsExactly(login);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getId).containsExactly(id);
    }

    @Test
    @DisplayName("user service: find all by university")
    void find_findsAllUsersByUniversity_whenDataIsValid() {
        UserFilter filter = new UserFilter();

        Page<User> page = new Page<>(List.of(user), pageable, 1L);
        PageResponse<UserDto> pageResponse = new PageResponse<>(List.of(returnedUserDto), pageableResponse, 1L);

        when(userMapper.toEntity(any(UserFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(userDao.findAll(filter, pageable)).thenReturn(page);
        when(userMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<UserDto> filterSearchResult = userService.findAll(new UserFilterRequest(), new PageableRequest());

        assertThat(filterSearchResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getUsername).containsExactly(login);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getId).containsExactly(id);
    }
}
