package com.github.blog.controller;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.AuthenticationRequest;
import com.github.blog.controller.dto.request.RegistrationRequest;
import com.github.blog.controller.dto.response.JwtResponse;
import com.github.blog.service.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Raman Haurylau
 */
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("sign-up")
    public UserDto signUp(@RequestBody RegistrationRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("sign-in")
    public JwtResponse authenticateAndGetToken(@RequestBody AuthenticationRequest request) {
        return authenticationService.signIn(request);
    }
}
