package com.github.blog.service;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.response.JwtResponse;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
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
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private UserRequest userRequest;
    private UserDto userDto;

    private final Long id = 1L;
    private final String username = "testuser";
    private final String password = "password";
    private final String encodedPassword = "encodedPassword";

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest.setUsername(username);
        userRequest.setPassword(password);

        userDto = new UserDto();
        userDto.setId(id);
        userDto.setUsername(username);
    }

    @Test
    @DisplayName("authentication service: sign up")
    void signUp_encodesPasswordAndCreatesUser() {
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userService.create(userRequest)).thenReturn(userDto);

        UserDto createdUser = authenticationService.signUp(userRequest);

        assertNotNull(createdUser);
        assertEquals(id, createdUser.getId());
        verify(passwordEncoder, times(1)).encode(password);
        verify(userService, times(1)).create(userRequest);
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

        JwtResponse response = authenticationService.signIn(userRequest);

        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());
        assertEquals(username, response.getUsername());
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

        CustomException exception = assertThrows(CustomException.class, () -> authenticationService.signIn(userRequest));

        assertEquals(ExceptionEnum.AUTHENTICATION_FAILED, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("authentication service: update user")
    void update_encodesPasswordAndUpdatesUser() {
        userRequest.setPassword(password);

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userService.update(id, userRequest)).thenReturn(userDto);

        UserDto updatedUser = authenticationService.update(id, userRequest);

        assertNotNull(updatedUser);
        assertEquals(id, updatedUser.getId());
        verify(passwordEncoder, times(1)).encode(password);
        verify(userService, times(1)).update(id, userRequest);
    }

    @Test
    @DisplayName("authentication service: update user without password")
    void update_updatesUserWithoutEncodingPassword_whenPasswordIsBlank() {
        userRequest.setPassword("");

        when(userService.update(id, userRequest)).thenReturn(userDto);

        UserDto updatedUser = authenticationService.update(id, userRequest);

        assertNotNull(updatedUser);
        assertEquals(id, updatedUser.getId());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userService, times(1)).update(id, userRequest);
    }
}
