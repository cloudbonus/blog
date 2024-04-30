package com.github.blog.service.impl;

import com.github.blog.dao.RoleDao;
import com.github.blog.dao.UserDao;
import com.github.blog.dto.common.UserDto;
import com.github.blog.dto.filter.UserDtoFilter;
import com.github.blog.dto.request.UserRequestFilter;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.service.mapper.UserMapper;
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
    UserDao userDao;
    @Mock
    RoleDao roleDao;
    @Mock
    UserMapper userMapper;
    @InjectMocks
    UserServiceImpl userService;

    private User user;
    private UserDto returnedUserDto;
    private UserDto userDto;

    private final Long id = 1L;
    private final String login = "test login";

    @BeforeEach
    void setUp() {
        String password = "test password";
        String email = "temp@test.temp";
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setLastLogin(updatedAt);

        returnedUserDto = new UserDto();
        returnedUserDto.setId(id);
        returnedUserDto.setLogin(login);
        returnedUserDto.setPassword(password);
        returnedUserDto.setEmail(email);
        returnedUserDto.setCreatedAt(createdAt);
        returnedUserDto.setLastLogin(updatedAt);

        userDto = new UserDto();
        userDto.setLogin(login);
        userDto.setPassword(password);
        userDto.setEmail(email);
        userDto.setCreatedAt(createdAt);
        userDto.setLastLogin(updatedAt);
    }

    @Test
    @DisplayName("user service: create")
    void create_returnsUserDto_whenDataIsValid() {
        String roleName = "ROLE_USER";
        Role role = new Role();
        role.setId(id);
        role.setRoleName(roleName);
        Optional<Role> optionalRole = Optional.of(role);

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userDao.create(user)).thenReturn(user);
        when(roleDao.findByName(roleName)).thenReturn(optionalRole);
        when(userDao.update(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);
        UserDto createdUserDto = userService.create(userDto);

        assertThat(createdUserDto).isNotNull();
        assertThat(createdUserDto.getId()).isEqualTo(id);
        assertThat(createdUserDto.getLogin()).isEqualTo(login);
    }

    @Test
    @DisplayName("user service: update")
    void update_returnsUpdatedUserDto_whenDataIsValid() {
        Optional<User> optionalUser = Optional.of(user);

        when(userDao.findById(id)).thenReturn(optionalUser);
        when(userMapper.partialUpdate(userDto, user)).thenReturn(user);
        when(userDao.update(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);
        UserDto updatedUserDto = userService.update(id, userDto);

        assertThat(updatedUserDto).isNotNull();
        assertThat(updatedUserDto.getId()).isEqualTo(id);
        assertThat(updatedUserDto.getLogin()).isEqualTo(login);
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
        assertThat(deletedUserDto.getLogin()).isEqualTo(login);
        verify(userDao, times(1)).delete(user);
    }

    @Test
    @DisplayName("user service: findAllByRole")
    void find_findsAllUsersByRole_whenDataIsValid() {
        String role = "ROLE_USER";
        List<User> users = List.of(user);
        UserDtoFilter dtoFilter = new UserDtoFilter();
        dtoFilter.setRole(role);

        UserRequestFilter requestFilter = new UserRequestFilter();
        requestFilter.setRole(role);

        when(userMapper.toDto(requestFilter)).thenReturn(dtoFilter);
        when(userDao.findAll(dtoFilter)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        List<UserDto> filterSearchResult = userService.findAll(requestFilter);

        assertThat(filterSearchResult).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult).extracting(UserDto::getLogin).containsExactly(login);
        assertThat(filterSearchResult).extracting(UserDto::getId).containsExactly(id);
    }

    @Test
    @DisplayName("user: findByLogin")
    void find_findsUserByLogin_whenDataIsValid() {
        List<User> users = List.of(user);
        UserDtoFilter dtoFilter = new UserDtoFilter();
        dtoFilter.setLogin(user.getLogin());

        UserRequestFilter requestFilter = new UserRequestFilter();
        requestFilter.setLogin(user.getLogin());

        when(userMapper.toDto(requestFilter)).thenReturn(dtoFilter);
        when(userDao.findAll(dtoFilter)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        List<UserDto> filterSearchResult = userService.findAll(requestFilter);

        assertThat(filterSearchResult).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult).extracting(UserDto::getLogin).containsExactly(login);
        assertThat(filterSearchResult).extracting(UserDto::getId).containsExactly(id);
    }

    @Test
    @DisplayName("user service: findAllByJobTitle")
    void find_findsAllUsersByJobTitle_whenDataIsValid() {
        String jobTitle = "Software Engineer";
        List<User> users = List.of(user);

        UserDtoFilter dtoFilter = new UserDtoFilter();
        dtoFilter.setJob(jobTitle);

        UserRequestFilter requestFilter = new UserRequestFilter();
        requestFilter.setJob(jobTitle);

        when(userMapper.toDto(requestFilter)).thenReturn(dtoFilter);
        when(userDao.findAll(dtoFilter)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        List<UserDto> filterSearchResult = userService.findAll(requestFilter);

        assertThat(filterSearchResult).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult).extracting(UserDto::getLogin).containsExactly(login);
        assertThat(filterSearchResult).extracting(UserDto::getId).containsExactly(id);
    }

    @Test
    @DisplayName("user service: findAllByUniversity")
    void find_findsAllUsersByUniversity_whenDataIsValid() {
        String university = "MIT";
        List<User> users = List.of(user);

        UserDtoFilter dtoFilter = new UserDtoFilter();
        dtoFilter.setUniversity(university);

        UserRequestFilter requestFilter = new UserRequestFilter();
        requestFilter.setUniversity(university);

        when(userMapper.toDto(requestFilter)).thenReturn(dtoFilter);
        when(userDao.findAll(dtoFilter)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        List<UserDto> filterSearchResult = userService.findAll(requestFilter);

        assertThat(filterSearchResult).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult).extracting(UserDto::getLogin).containsExactly(login);
        assertThat(filterSearchResult).extracting(UserDto::getId).containsExactly(id);
    }
}
