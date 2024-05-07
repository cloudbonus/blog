package com.github.blog.controller;

import com.github.blog.controller.dto.request.AuthRequest;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.response.JwtResponse;
import com.github.blog.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

/**
 * @author Raman Haurylau
 */
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @GetMapping("welcome")
    @PreAuthorize("isAuthenticated()")
    public Map<String, String> welcome() {
        return Collections.singletonMap("test", "authenticated");
    }

    @GetMapping("user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Map<String, String> userProfile() {
        return Collections.singletonMap("test", "User");
    }

    @PostMapping("sign-up")
    public JwtResponse signUp(@RequestBody UserRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("sign-in")
    public JwtResponse authenticateAndGetToken(@RequestBody AuthRequest request) {
        return authenticationService.signIn(request);
    }

}
