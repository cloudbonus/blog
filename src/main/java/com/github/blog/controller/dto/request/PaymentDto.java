package com.github.blog.controller.dto.request;

import com.github.blog.repository.entity.util.OrderState;

/**
 * @author Raman Haurylau
 */
public record PaymentDto(Long id, Long paymentId, String fullName, OrderState state) {
}
