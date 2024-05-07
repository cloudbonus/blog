package com.github.blog.security;

import com.github.blog.config.jwtconfig.JwtService;
import com.github.blog.controller.dto.request.AuthRequest;
import com.github.blog.controller.dto.request.UserRequest;
import com.github.blog.controller.dto.response.JwtResponse;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.repository.RoleDao;
import com.github.blog.repository.UserDao;
import com.github.blog.service.UserService;
import com.github.blog.service.exception.RoleErrorResult;
import com.github.blog.service.exception.impl.RoleException;
import com.github.blog.service.mapper.UserMapper;
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
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final RoleDao roleDao;


    public JwtResponse signUp(UserRequest request) {
        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userDao.create(user);

        Role role = roleDao
                .findByName("ROLE_USER")
                .orElseThrow(() -> new RoleException(RoleErrorResult.ROLE_NOT_FOUND));

        user.getRoles().add(role);
        role.getUsers().add(user);

        roleDao.update(role);
        user = userDao.update(user);

        String jwt = jwtService.generateToken(user);

        return new JwtResponse(jwt, user.getUsername());
    }

    public JwtResponse signIn(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetails user = userService.userDetailsService().loadUserByUsername(request.getUsername());

        String jwt = jwtService.generateToken(user);

        return new JwtResponse(jwt, user.getUsername());
    }
}
