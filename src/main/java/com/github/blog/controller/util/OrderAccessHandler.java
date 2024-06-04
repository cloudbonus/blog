package com.github.blog.controller.util;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.service.OrderService;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.security.AuthenticatedUserService;
import com.github.blog.service.statemachine.state.OrderState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Raman Haurylau
 */
@Component("orderAccess")
@RequiredArgsConstructor
public class OrderAccessHandler {

    private final AuthenticatedUserService authenticatedUserService;

    private final OrderService orderService;

    public boolean isOrderCompleted(Long id) {
        try {
            OrderDto orderDto = orderService.findByPostId(id);
            return orderDto.getState().equals(OrderState.COMPLETED.name());
        } catch (CustomException e) {
            return true;
        }
    }

    public boolean verifyOwnership(Long id) {
        try {
            Long sessionUserId = authenticatedUserService.getAuthenticatedUser().getId();
            OrderDto orderDto = orderService.findById(id);
            return orderDto.getUserId().equals(sessionUserId);
        } catch (CustomException e) {
            return false;
        }
    }

    public boolean canFilter(Long id) {
        return id != null && id.equals(authenticatedUserService.getAuthenticatedUser().getId());
    }
}
