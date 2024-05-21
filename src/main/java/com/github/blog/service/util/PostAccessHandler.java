package com.github.blog.service.util;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.filter.PostDtoFilter;
import com.github.blog.service.OrderService;
import com.github.blog.service.exception.impl.OrderException;
import com.github.blog.service.security.AuthenticatedUserService;
import com.github.blog.service.statemachine.state.OrderState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Raman Haurylau
 */
@Component("postAccess")
@RequiredArgsConstructor
public class PostAccessHandler {
    private final AuthenticatedUserService authenticatedUserService;
    private final OrderService orderService;

    public boolean verifyPostPurchase(Long id) {
        try {
            OrderDto orderDto = orderService.findByPostId(id);
            return orderDto.getUserId().equals(authenticatedUserService.getAuthenticatedUser().getId());
        } catch (OrderException e) {
            return true;
        }
    }

    public boolean hasRole(String roleName) {
        return authenticatedUserService.getAuthenticatedUser().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
    }

    public boolean canFilter(PostDtoFilter requestFilter) {
        if (!requestFilter.getState().equals(OrderState.COMPLETED.name()) && requestFilter.getUsername() != null) {
            return requestFilter.getUsername().equals(authenticatedUserService.getAuthenticatedUser().getUsername());
        }
        return true;
    }
}
