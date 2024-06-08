package com.github.blog.service;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.UserFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.dto.response.PageableResponse;
import com.github.blog.model.User;
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
    private UserMapper userMapper;

    @Mock
    private PageableMapper pageableMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto returnedUserDto;
    private final User user = new User();

    private final Long id = 1L;
    private final String username = "username";

    private final Pageable pageable = new Pageable();
    private final PageableRequest pageableRequest = new PageableRequest(null, null, null);
    private final UserFilterRequest userFilterRequest = new UserFilterRequest(null, null, null, null, null, null, null, null);
    private final PageableResponse pageableResponse = new PageableResponse(0, 0, null);

    @BeforeEach
    void setUp() {
        String password = "password";
        returnedUserDto = new UserDto(id, username, password, null, null, null);
    }

    @Test
    @DisplayName("user service: delete")
    void delete_deletesUser_whenDataIsValid() {
        when(userDao.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        UserDto deletedUserDto = userService.delete(id);

        assertThat(deletedUserDto).isNotNull();
        assertThat(deletedUserDto.id()).isEqualTo(id);
        assertThat(deletedUserDto.username()).isEqualTo(username);
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

        PageResponse<UserDto> filterSearchResult = userService.findAll(userFilterRequest, pageableRequest);

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(UserDto::username).containsExactly(username);
        assertThat(filterSearchResult.content()).extracting(UserDto::id).containsExactly(id);
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

        PageResponse<UserDto> filterSearchResult = userService.findAll(userFilterRequest, pageableRequest);

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(UserDto::username).containsExactly(username);
        assertThat(filterSearchResult.content()).extracting(UserDto::id).containsExactly(id);
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

        PageResponse<UserDto> filterSearchResult = userService.findAll(userFilterRequest, pageableRequest);

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(UserDto::username).containsExactly(username);
        assertThat(filterSearchResult.content()).extracting(UserDto::id).containsExactly(id);
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

        PageResponse<UserDto> filterSearchResult = userService.findAll(userFilterRequest, pageableRequest);

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(UserDto::username).containsExactly(username);
        assertThat(filterSearchResult.content()).extracting(UserDto::id).containsExactly(id);
    }
}
