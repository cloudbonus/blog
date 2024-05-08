package com.github.blog.service.security.impl;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.AuthenticationRequest;
import com.github.blog.controller.dto.request.RegistrationRequest;
import com.github.blog.controller.dto.response.JwtResponse;
import com.github.blog.service.UserService;
import com.github.blog.service.exception.UserErrorResult;
import com.github.blog.service.exception.impl.UserException;
import com.github.blog.service.security.AuthenticationService;
import com.github.blog.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Transactional
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    public UserDto signUp(RegistrationRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return userService.create(request);
    }

    public JwtResponse signIn(AuthenticationRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(!auth.isAuthenticated()) {
            throw new UserException(UserErrorResult.AUTHENTICATION_FAILED);
        }
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());

        String jwt = jwtService.generateToken(user);

        return new JwtResponse(jwt, user.getUsername());
    }
}
