package com.github.blog.service.security;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.AuthenticationRequest;
import com.github.blog.controller.dto.request.RegistrationRequest;
import com.github.blog.controller.dto.response.JwtResponse;

/**
 * @author Raman Haurylau
 */
public interface AuthenticationService {
    UserDto signUp(RegistrationRequest request);

    JwtResponse signIn(AuthenticationRequest request);

    UserDto update(Long id, RegistrationRequest request);
}
