package com.github.blog.service;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.OrderRequest;

import java.util.List;


/**
 * @author Raman Haurylau
 */
public interface OrderService {
    List<OrderDto> findAll();

    OrderDto create(OrderRequest t);

    OrderDto findById(Long id);

    OrderDto update(Long id, OrderRequest t);

    OrderDto delete(Long id);
}
