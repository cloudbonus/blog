package com.github.blog.service.security;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.response.JwtResponse;

/**
 * @author Raman Haurylau
 */
public interface AuthenticationService {
    UserDto signUp(UserRequest request);

    JwtResponse signIn(UserRequest request);

    UserDto update(Long id, UserRequest request);
}
