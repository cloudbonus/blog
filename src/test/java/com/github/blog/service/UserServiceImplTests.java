package com.github.blog.service;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.UserFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.User;
import com.github.blog.repository.UserRepository;
import com.github.blog.repository.filter.UserFilter;
import com.github.blog.service.impl.UserServiceImpl;
import com.github.blog.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
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
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto returnedUserDto;
    private final User user = new User();

    private final Long id = 1L;
    private final String username = "username";

    private final PageableRequest pageableRequest = new PageableRequest(10, null, "asc");
    private final UserFilterRequest userFilterRequest = new UserFilterRequest(null, null, null, null, null, null, null, null);

    @BeforeEach
    void setUp() {
        String password = "password";
        returnedUserDto = new UserDto(id, username, password, null, null, null);
    }

    @Test
    @DisplayName("user service: delete")
    void delete_deletesUser_whenDataIsValid() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        UserDto deletedUserDto = userService.delete(id);

        assertThat(deletedUserDto).isNotNull();
        assertThat(deletedUserDto.id()).isEqualTo(id);
        assertThat(deletedUserDto.username()).isEqualTo(username);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("user service: find all by role")
    void find_findsAllUsersByRole_whenDataIsValid() {
        UserFilter filter = new UserFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<User> page = new PageImpl<>(List.of(user), pageable, 1L);
        PageResponse<UserDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedUserDto), 1, 1, 0, 1);

        when(userMapper.toEntity(any(UserFilterRequest.class))).thenReturn(filter);
        when(userRepository.findAll(any(Specification.class),  any(Pageable.class))).thenReturn(page);
        when(userMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<UserDto> filterSearchResult = userService.findAll(userFilterRequest, pageableRequest);

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(UserDto::username).containsExactly(username);
        assertThat(filterSearchResult.content()).extracting(UserDto::id).containsExactly(id);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("user: find by username")
    void find_findsUserByUsername_whenDataIsValid() {
        UserFilter filter = new UserFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<User> page = new PageImpl<>(List.of(user), pageable, 1L);
        PageResponse<UserDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedUserDto), 1, 1, 0, 1);

        when(userMapper.toEntity(any(UserFilterRequest.class))).thenReturn(filter);
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(userMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<UserDto> filterSearchResult = userService.findAll(userFilterRequest, pageableRequest);

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(UserDto::username).containsExactly(username);
        assertThat(filterSearchResult.content()).extracting(UserDto::id).containsExactly(id);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("user service: find all by job")
    void find_findsAllUsersByJob_whenDataIsValid() {
        UserFilter filter = new UserFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<User> page = new PageImpl<>(List.of(user), pageable, 1L);
        PageResponse<UserDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedUserDto), 1, 1, 0, 1);

        when(userMapper.toEntity(any(UserFilterRequest.class))).thenReturn(filter);
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(userMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<UserDto> filterSearchResult = userService.findAll(userFilterRequest, pageableRequest);

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(UserDto::username).containsExactly(username);
        assertThat(filterSearchResult.content()).extracting(UserDto::id).containsExactly(id);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("user service: find all by university")
    void find_findsAllUsersByUniversity_whenDataIsValid() {
        UserFilter filter = new UserFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<User> page = new PageImpl<>(List.of(user), pageable, 1L);
        PageResponse<UserDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedUserDto), 1, 1, 0, 1);

        when(userMapper.toEntity(any(UserFilterRequest.class))).thenReturn(filter);
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(userMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<UserDto> filterSearchResult = userService.findAll(userFilterRequest, pageableRequest);

        assertThat(filterSearchResult.content()).isNotEmpty().hasSize(1);
        assertThat(filterSearchResult.content()).extracting(UserDto::username).containsExactly(username);
        assertThat(filterSearchResult.content()).extracting(UserDto::id).containsExactly(id);
    }
}
