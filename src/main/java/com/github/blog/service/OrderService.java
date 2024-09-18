package com.github.blog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.OrderRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.PaymentDto;
import com.github.blog.controller.dto.request.PaymentRequest;
import com.github.blog.controller.dto.request.filter.OrderFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;


/**
 * @author Raman Haurylau
 */
public interface OrderService {
    PageResponse<OrderDto> findAll(OrderFilterRequest requestFilter, PageableRequest pageableRequest);

    PaymentRequest cancel(Long id);

    PaymentRequest process(Long id) throws JsonProcessingException;

    OrderDto findById(Long id);

    OrderDto update(Long id, OrderRequest t);

    OrderDto delete(Long id);

    OrderDto findByPostId(Long id);

    void updateState(PaymentDto paymentDto);
}
