package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.UserDtoFilter;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.repository.RoleDao;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.UserFilter;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    private User user;
    private UserDto returnedUserDto;
    private UserRequest request;

    private final Long id = 1L;
    private final String login = "test login";

    private static Pageable pageable;
    private static PageableRequest pageableRequest;

    @BeforeAll
    public static void setUpPageable() {
        pageableRequest = new PageableRequest();

        pageable = new Pageable();
        pageable.setPageSize(Integer.MAX_VALUE);
        pageable.setPageNumber(1);
    }

    @BeforeEach
    void setUp() {
        String password = "test password";
        String email = "temp@test.temp";
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        user = new User();
        user.setId(id);
        user.setUsername(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setLastLogin(updatedAt);

        returnedUserDto = new UserDto();
        returnedUserDto.setId(id);
        returnedUserDto.setUsername(login);
        returnedUserDto.setPassword(password);
        returnedUserDto.setEmail(email);
        returnedUserDto.setCreatedAt(createdAt);
        returnedUserDto.setLastLogin(updatedAt);

        request = new UserRequest();
        request.setUsername(login);
        request.setPassword(password);
        request.setEmail(email);
    }

    @Test
    @DisplayName("user service: create")
    void create_returnsUserDto_whenDataIsValid() {
        String roleName = "ROLE_USER";
        Role role = new Role();
        role.setId(id);
        role.setRoleName(roleName);
        Optional<Role> optionalRole = Optional.of(role);

        when(userMapper.toEntity(request)).thenReturn(user);
        when(userDao.create(user)).thenReturn(user);
        when(roleDao.findByName(roleName)).thenReturn(optionalRole);
        when(userDao.update(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);
        UserDto createdUserDto = userService.create(request);

        assertThat(createdUserDto).isNotNull();
        assertThat(createdUserDto.getId()).isEqualTo(id);
        assertThat(createdUserDto.getUsername()).isEqualTo(login);
    }

    @Test
    @DisplayName("user service: update")
    void update_returnsUpdatedUserDto_whenDataIsValid() {
        Optional<User> optionalUser = Optional.of(user);

        when(userDao.findById(id)).thenReturn(optionalUser);
        when(userMapper.partialUpdate(request, user)).thenReturn(user);
        when(userDao.update(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);
        UserDto updatedUserDto = userService.update(id, request);

        assertThat(updatedUserDto).isNotNull();
        assertThat(updatedUserDto.getId()).isEqualTo(id);
        assertThat(updatedUserDto.getUsername()).isEqualTo(login);
    }

    @Test
    @DisplayName("user service: delete")
    void delete_deletesUser_whenDataIsValid() {
        Optional<User> optionalUser = Optional.of(user);

        when(userDao.findById(id)).thenReturn(optionalUser);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        UserDto deletedUserDto = userService.delete(id);

        assertThat(deletedUserDto).isNotNull();
        assertThat(deletedUserDto.getId()).isEqualTo(id);
        assertThat(deletedUserDto.getUsername()).isEqualTo(login);
        verify(userDao, times(1)).delete(user);
    }

    @Test
    @DisplayName("user service: findAllByRole")
    void find_findsAllUsersByRole_whenDataIsValid() {
        String role = "ROLE_USER";
        UserFilter dtoFilter = new UserFilter();
        dtoFilter.setRole(role);

        UserDtoFilter requestFilter = new UserDtoFilter();
        requestFilter.setRole(role);

        Page<User> users = new Page<>(List.of(user), pageable, 1L);

        when(userMapper.toDto(requestFilter)).thenReturn(dtoFilter);
        when(pageableMapper.toDto(pageableRequest)).thenReturn(pageable);
        when(userDao.findAll(dtoFilter, pageable)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        Page<UserDto> filterSearchResult = userService.findAll(requestFilter, pageableRequest);

        assertThat(filterSearchResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getUsername).containsExactly(login);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getId).containsExactly(id);
    }

    @Test
    @DisplayName("user: findByLogin")
    void find_findsUserByLogin_whenDataIsValid() {
        Page<User> users = new Page<>(List.of(user), pageable, 1L);

        UserFilter dtoFilter = new UserFilter();
        dtoFilter.setLogin(user.getUsername());

        UserDtoFilter requestFilter = new UserDtoFilter();
        requestFilter.setLogin(user.getUsername());

        when(userMapper.toDto(requestFilter)).thenReturn(dtoFilter);
        when(pageableMapper.toDto(pageableRequest)).thenReturn(pageable);
        when(userDao.findAll(dtoFilter, pageable)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        Page<UserDto> filterSearchResult = userService.findAll(requestFilter, pageableRequest);

        assertThat(filterSearchResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getUsername).containsExactly(login);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getId).containsExactly(id);
    }

    @Test
    @DisplayName("user service: findAllByJobTitle")
    void find_findsAllUsersByJobTitle_whenDataIsValid() {
        String jobTitle = "Software Engineer";
        Page<User> users = new Page<>(List.of(user), pageable, 1L);

        UserFilter dtoFilter = new UserFilter();
        dtoFilter.setJob(jobTitle);

        UserDtoFilter requestFilter = new UserDtoFilter();
        requestFilter.setJob(jobTitle);

        when(userMapper.toDto(requestFilter)).thenReturn(dtoFilter);
        when(pageableMapper.toDto(pageableRequest)).thenReturn(pageable);
        when(userDao.findAll(dtoFilter, pageable)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        Page<UserDto> filterSearchResult = userService.findAll(requestFilter, pageableRequest);

        assertThat(filterSearchResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getUsername).containsExactly(login);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getId).containsExactly(id);
    }

    @Test
    @DisplayName("user service: findAllByUniversity")
    void find_findsAllUsersByUniversity_whenDataIsValid() {
        String university = "MIT";
        Page<User> users = new Page<>(List.of(user), pageable, 1L);

        UserFilter dtoFilter = new UserFilter();
        dtoFilter.setUniversity(university);

        UserDtoFilter requestFilter = new UserDtoFilter();
        requestFilter.setUniversity(university);

        when(userMapper.toDto(requestFilter)).thenReturn(dtoFilter);
        when(pageableMapper.toDto(pageableRequest)).thenReturn(pageable);
        when(userDao.findAll(dtoFilter, pageable)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        Page<UserDto> filterSearchResult = userService.findAll(requestFilter, pageableRequest);

        assertThat(filterSearchResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getUsername).containsExactly(login);
        assertThat(filterSearchResult.getContent()).extracting(UserDto::getId).containsExactly(id);
    }
}
