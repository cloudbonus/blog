package com.github.blog.service.impl;

import com.github.blog.dao.RoleDao;
import com.github.blog.dao.UserDao;
import com.github.blog.dto.UserDto;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.service.mapper.UserMapper;
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

    @Test
    @DisplayName("user service: create")
    void create_returnsUserDto_whenDataIsValid() {
        Long id = 1L;
        String login = "test_login";
        String password = "test_password";
        String email = "temp@test.temp";
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        UserDto userDto = new UserDto();
        userDto.setLogin(login);
        userDto.setPassword(password);
        userDto.setEmail(email);
        userDto.setCreatedAt(createdAt);
        userDto.setLastLogin(updatedAt);

        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setLastLogin(updatedAt);

        UserDto returnedUserDto = new UserDto();
        returnedUserDto.setId(id);
        returnedUserDto.setLogin(login);
        returnedUserDto.setPassword(password);
        returnedUserDto.setEmail(email);
        returnedUserDto.setCreatedAt(createdAt);
        returnedUserDto.setLastLogin(updatedAt);

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
        UserDto createdUser = userService.create(userDto);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isEqualTo(id);
        assertThat(createdUser.getLogin()).isEqualTo(login);
    }

    @Test
    @DisplayName("user service: update")
    void update_returnsUpdatedUserDto_whenDataIsValid() {
        Long id = 1L;
        String login = "test_login";
        String password = "test_password";
        String email = "temp@test.temp";
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        UserDto userDto = new UserDto();
        userDto.setLogin(login);
        userDto.setPassword(password);
        userDto.setEmail(email);
        userDto.setCreatedAt(createdAt);
        userDto.setLastLogin(updatedAt);

        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setLastLogin(updatedAt);
        Optional<User> optionalUser = Optional.of(user);

        UserDto returnedUserDto = new UserDto();
        returnedUserDto.setId(id);
        returnedUserDto.setLogin(login);
        returnedUserDto.setPassword(password);
        returnedUserDto.setEmail(email);
        returnedUserDto.setCreatedAt(createdAt);
        returnedUserDto.setLastLogin(updatedAt);

        when(userDao.findById(id)).thenReturn(optionalUser);
        when(userMapper.partialUpdate(userDto, user)).thenReturn(user);
        when(userDao.update(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);
        UserDto updatedUser = userService.update(id, userDto);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(id);
        assertThat(updatedUser.getLogin()).isEqualTo(login);
    }

    @Test
    @DisplayName("user service: delete")
    void delete_deletesUser_whenDataIsValid() {
        Long id = 1L;
        String login = "test_login";
        String password = "test_password";
        String email = "temp@test.temp";
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setLastLogin(updatedAt);
        Optional<User> optionalUser = Optional.of(user);

        UserDto returnedUserDto = new UserDto();
        returnedUserDto.setId(id);
        returnedUserDto.setLogin(login);
        returnedUserDto.setPassword(password);
        returnedUserDto.setEmail(email);
        returnedUserDto.setCreatedAt(createdAt);
        returnedUserDto.setLastLogin(updatedAt);

        when(userDao.findById(id)).thenReturn(optionalUser);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        UserDto deletedUser = userService.delete(id);

        assertThat(deletedUser).isNotNull();
        assertThat(deletedUser.getId()).isEqualTo(id);
        assertThat(deletedUser.getLogin()).isEqualTo(login);
        verify(userDao, times(1)).delete(user);
    }

    @Test
    @DisplayName("user service: findAllByRole")
    void find_findsAllUsersByRole_whenDataIsValid() {
        Long id = 1L;
        String login = "test_login";
        String password = "test_password";
        String email = "temp@test.temp";
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setLastLogin(updatedAt);

        UserDto returnedUserDto = new UserDto();
        returnedUserDto.setId(id);
        returnedUserDto.setLogin(login);
        returnedUserDto.setPassword(password);
        returnedUserDto.setEmail(email);
        returnedUserDto.setCreatedAt(createdAt);
        returnedUserDto.setLastLogin(updatedAt);

        String roleName = "ROLE_USER";
        List<User> users = List.of(user);

        when(userDao.findAllByRole(roleName)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        List<UserDto> allUsersByRole = userService.findAllByRole(roleName);

        assertThat(allUsersByRole).isNotEmpty().hasSize(1);
        assertThat(allUsersByRole).extracting(UserDto::getLogin).containsExactly(login);
        assertThat(allUsersByRole).extracting(UserDto::getId).containsExactly(id);
    }

    @Test
    @DisplayName("user: findByLogin")
    void find_findsUserByLogin_whenDataIsValid() {
        Long id = 1L;
        String login = "test_login";
        String password = "test_password";
        String email = "temp@test.temp";
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setLastLogin(updatedAt);
        Optional<User> optionalUser = Optional.of(user);

        UserDto returnedUserDto = new UserDto();
        returnedUserDto.setId(id);
        returnedUserDto.setLogin(login);
        returnedUserDto.setPassword(password);
        returnedUserDto.setEmail(email);
        returnedUserDto.setCreatedAt(createdAt);
        returnedUserDto.setLastLogin(updatedAt);

        when(userDao.findByLogin(login)).thenReturn(optionalUser);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        UserDto foundUserDto = userService.findByLogin(login);

        assertThat(foundUserDto).isNotNull();
        assertThat(foundUserDto.getId()).isEqualTo(id);
        assertThat(foundUserDto.getLogin()).isEqualTo(login);
    }

    @Test
    @DisplayName("user service: findAllByJobTitle")
    void find_findsAllUsersByJobTitle_whenDataIsValid() {
        Long id = 1L;
        String login = "test_login";
        String password = "test_password";
        String email = "temp@test.temp";
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setLastLogin(updatedAt);

        UserDto returnedUserDto = new UserDto();
        returnedUserDto.setId(id);
        returnedUserDto.setLogin(login);
        returnedUserDto.setPassword(password);
        returnedUserDto.setEmail(email);
        returnedUserDto.setCreatedAt(createdAt);
        returnedUserDto.setLastLogin(updatedAt);

        String jobTitle = "Software Engineer";
        List<User> users = List.of(user);

        when(userDao.findAllByJobTitle(jobTitle)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        List<UserDto> allByJobTitle = userService.findAllByJobTitle(jobTitle);

        assertThat(allByJobTitle).isNotEmpty().hasSize(1);
        assertThat(allByJobTitle).extracting(UserDto::getLogin).containsExactly(login);
        assertThat(allByJobTitle).extracting(UserDto::getId).containsExactly(id);
    }

    @Test
    @DisplayName("user service: findAllByUniversity")
    void find_findsAllUsersByUniversity_whenDataIsValid() {
        Long id = 1L;
        String login = "test_login";
        String password = "test_password";
        String email = "temp@test.temp";
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setLastLogin(updatedAt);

        UserDto returnedUserDto = new UserDto();
        returnedUserDto.setId(id);
        returnedUserDto.setLogin(login);
        returnedUserDto.setPassword(password);
        returnedUserDto.setEmail(email);
        returnedUserDto.setCreatedAt(createdAt);
        returnedUserDto.setLastLogin(updatedAt);

        String university = "MIT";
        List<User> users = List.of(user);

        when(userDao.findAllByUniversity(university)).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        List<UserDto> allByJobTitle = userService.findAllByUniversity(university);

        assertThat(allByJobTitle).isNotEmpty().hasSize(1);
        assertThat(allByJobTitle).extracting(UserDto::getLogin).containsExactly(login);
        assertThat(allByJobTitle).extracting(UserDto::getId).containsExactly(id);
    }
}
