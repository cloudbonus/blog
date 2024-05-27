package com.github.blog.controller;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.response.JwtResponse;
import com.github.blog.controller.util.marker.UserValidationGroups;
import com.github.blog.service.security.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Raman Haurylau
 */
@Validated
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("sign-up")
    @Validated(UserValidationGroups.UserCreateValidationGroupSequence.class)
    public UserDto signUp(@RequestBody @Valid UserRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("sign-in")
    @Validated(UserValidationGroups.UserAuthenticateValidationGroupSequence.class)
    public JwtResponse authenticateAndGetToken(@RequestBody @Valid UserRequest request) {
        return authenticationService.signIn(request);
    }
}
