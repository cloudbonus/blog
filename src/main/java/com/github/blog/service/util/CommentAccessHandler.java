package com.github.blog.service.util;

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
@Component("commentAccess")
@RequiredArgsConstructor
public class CommentAccessHandler {
    private final OrderService orderService;

    public boolean verifyPostPurchase(Long id) {
        try {
            OrderDto orderDto = orderService.findByPostId(id);
            return orderDto.getState().equals(OrderState.COMPLETED.name());
        } catch (CustomException e) {
            return true;
        }
    }
}
