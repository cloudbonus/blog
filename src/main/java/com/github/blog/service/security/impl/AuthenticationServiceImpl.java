package com.github.blog.service.security.impl;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.response.JwtResponse;
import com.github.blog.repository.RoleRepository;
import com.github.blog.repository.UserRepository;
import com.github.blog.repository.entity.Role;
import com.github.blog.repository.entity.User;
import com.github.blog.repository.entity.util.RoleEnum;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.UserMapper;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public UserDto signUp(UserRequest request) {
        log.debug("Signing up user with username: {}", request.username());
        User user = userMapper.toEntity(request);

        Role role = roleRepository
                .findByNameIgnoreCase(RoleEnum.ROLE_USER.name())
                .orElseThrow(() -> new CustomException(ExceptionEnum.ROLE_NOT_FOUND));

        user.getRoles().add(role);

        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
        log.debug("User signed up successfully with username: {}", request.username());
        return userMapper.toDto(user);
    }

    @Override
    public JwtResponse signIn(UserRequest request) {
        log.debug("Signing in user with username: {}", request.username());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        if (!authentication.isAuthenticated()) {
            throw new CustomException(ExceptionEnum.AUTHENTICATION_FAILED);
        }

        UserDetails user = userDetailsService.loadUserByUsername(request.username());
        String jwt = jwtService.generateToken(user);
        log.debug("User signed in successfully with username: {}", request.username());

        return new JwtResponse(jwt, user.getUsername());
    }

    @Override
    public UserDto update(Long id, UserRequest request) {
        log.debug("Updating user with ID: {}", id);

        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        user = userMapper.partialUpdate(request, user);

        if (!StringUtils.isBlank(request.password())) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        log.debug("User updated successfully with ID: {}", id);
        return userMapper.toDto(user);
    }
}