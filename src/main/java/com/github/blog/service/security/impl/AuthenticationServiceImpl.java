package com.github.blog.service.security.impl;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.response.JwtResponse;
import com.github.blog.service.UserService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.security.AuthenticationService;
import com.github.blog.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public UserDto signUp(UserRequest request) {
        log.debug("Signing up user with username: {}", request.getUsername());
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        UserDto userDto = userService.create(request);
        log.debug("User signed up successfully with username: {}", request.getUsername());
        return userDto;
    }

    @Override
    public JwtResponse signIn(UserRequest request) {
        log.debug("Signing in user with username: {}", request.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        if (!authentication.isAuthenticated()) {
            log.error("Authentication failed for username: {}", request.getUsername());
            throw new CustomException(ExceptionEnum.AUTHENTICATION_FAILED);
        }

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String jwt = jwtService.generateToken(user);
        log.debug("User signed in successfully with username: {}", request.getUsername());

        return new JwtResponse(jwt, user.getUsername());
    }

    @Override
    public UserDto update(Long id, UserRequest request) {
        log.debug("Updating user with ID: {}", id);
        if (!StringUtils.isBlank(request.getPassword())) {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        UserDto userDto = userService.update(id, request);
        log.debug("User updated successfully with ID: {}", id);
        return userDto;
    }
}