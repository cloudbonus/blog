package com.github.blog.service;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.response.JwtResponse;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.repository.RoleRepository;
import com.github.blog.repository.UserRepository;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.UserMapper;
import com.github.blog.service.security.JwtService;
import com.github.blog.service.security.impl.AuthenticationServiceImpl;
import com.github.blog.service.security.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Raman Haurylau
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private final Long id = 1L;

    private final User user = new User();

    private UserDto returnedUserDto;
    private UserRequest request;

    private final String username = "testuser";
    private final String password = "password";


    @BeforeEach
    void setUp() {
        request = new UserRequest(username, password, null);
        returnedUserDto = new UserDto(id, username, password, null, null, null);
    }

    @Test
    @DisplayName("user service: create")
    void create_returnsUserDto_whenDataIsValid() {
        when(userMapper.toEntity(request)).thenReturn(user);
        when(roleRepository.findByNameIgnoreCase("ROLE_USER")).thenReturn(Optional.of(new Role()));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        UserDto createdUserDto = authenticationService.signUp(request);

        assertNotNull(createdUserDto);
        assertEquals(id, createdUserDto.id());
        assertEquals(username, createdUserDto.username());
        verify(passwordEncoder, times(1)).encode(password);
    }

    @Test
    @DisplayName("user service: update")
    void update_returnsUpdatedUserDto_whenDataIsValid() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.partialUpdate(request, user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        UserDto updatedUserDto = authenticationService.update(id, request);

        assertNotNull(updatedUserDto);
        assertEquals(id, updatedUserDto.id());
        assertEquals(username, updatedUserDto.username());
        verify(passwordEncoder, times(1)).encode(password);
    }

    @Test
    @DisplayName("authentication service: update user without password")
    void update_updatesUserWithoutEncodingPassword_whenPasswordIsBlank() {
        UserRequest request = new UserRequest(username, null, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.partialUpdate(request, user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(returnedUserDto);

        UserDto updatedUserDto = authenticationService.update(id, request);

        assertNotNull(updatedUserDto);
        assertEquals(id, updatedUserDto.id());
        assertEquals(username, updatedUserDto.username());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("authentication service: sign in")
    void signIn_authenticatesUserAndGeneratesJwt() {
        String jwtToken = "jwtToken";

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userDetails.getUsername()).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(jwtToken);

        JwtResponse response = authenticationService.signIn(request);

        assertNotNull(response);
        assertEquals(jwtToken, response.token());
        assertEquals(username, response.username());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername(username);
        verify(jwtService, times(1)).generateToken(userDetails);
    }

    @Test
    @DisplayName("authentication service: sign in - authentication failed exception")
    void signIn_throwsException_whenAuthenticationFails() {
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> authenticationService.signIn(request));

        assertEquals(ExceptionEnum.AUTHENTICATION_FAILED, exception.getExceptionEnum());
    }
}
