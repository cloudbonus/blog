package com.github.blog.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.controller.advice.ErrorResponse;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.security.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private static final String TOKEN = "Bearer";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwt = resolveToken(request);
        String username = null;

        if (jwt != null) {
            try {
                username = jwtService.extractUserName(jwt);
                log.debug("Extracted username from JWT: {}", username);
            } catch (JwtException e) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);

                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getOutputStream(), new ErrorResponse("Not Acceptable", e.getMessage(), System.currentTimeMillis()));
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                log.debug("Loaded user details for username: {}", username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.debug("User authenticated: {}", username);
                }
            } catch (CustomException e) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);

                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getOutputStream(), new ErrorResponse(ExceptionEnum.AUTHENTICATION_TOKEN_EXCEPTION.name(), ExceptionEnum.AUTHENTICATION_TOKEN_EXCEPTION.getMessage(), System.currentTimeMillis()));
                return;
            }
        }

        log.debug("JWT authentication filter completed");
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest httpServletRequest) {
        String bearer = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith(TOKEN)) {
            log.debug("Bearer token found in Authorization header");
            return bearer.substring(7);
        }
        log.debug("No Bearer token found in Authorization header");
        return null;
    }
}
