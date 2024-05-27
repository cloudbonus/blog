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
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return userService.create(request);
    }

    @Override
    public JwtResponse signIn(UserRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        if (!auth.isAuthenticated()) {
            throw new CustomException(ExceptionEnum.AUTHENTICATION_FAILED);
        }

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());

        String jwt = jwtService.generateToken(user);

        return new JwtResponse(jwt, user.getUsername());
    }

    @Override
    public UserDto update(Long id, UserRequest request) {
        if (!StringUtils.isBlank(request.getPassword())) {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return userService.update(id, request);
    }
}
