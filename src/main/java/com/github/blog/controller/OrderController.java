package com.github.blog.controller;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.OrderRequest;
import com.github.blog.service.OrderService;
import com.github.blog.service.exception.UserErrorResult;
import com.github.blog.service.exception.impl.UserException;
import com.github.blog.service.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderDto create(@RequestBody OrderRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!userDetails.getId().equals(request.getUserId())) {
            throw new UserException(UserErrorResult.UNAUTHORIZED_CREATE_ATTEMPT);
        }
        return orderService.create(request);
    }

    @GetMapping("{id}")
    public OrderDto findById(@PathVariable("id") Long id) {
        return orderService.findById(id);
    }

    @GetMapping
    public List<OrderDto> findAll() {
        return orderService.findAll();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public OrderDto update(@PathVariable("id") Long id, @RequestBody OrderRequest request) {
        return orderService.update(id, request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public OrderDto delete(@PathVariable("id") Long id) {
        return orderService.findById(id);
    }
}

