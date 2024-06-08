package com.github.blog.controller;

import com.github.blog.controller.dto.common.UserDto;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.response.JwtResponse;
import com.github.blog.controller.util.marker.UserValidationGroup;
import com.github.blog.service.security.AuthenticationService;
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
    public UserDto signUp(@RequestBody @Validated(UserValidationGroup.onCreate.class) UserRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("sign-in")
    public JwtResponse authenticateAndGetToken(@RequestBody @Validated(UserValidationGroup.onAuthenticate.class) UserRequest request) {
        return authenticationService.signIn(request);
    }
}
