package com.github.blog.service;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.OrderRequest;
import com.github.blog.controller.dto.request.etc.PageableRequest;
import com.github.blog.controller.dto.request.filter.OrderFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;


/**
 * @author Raman Haurylau
 */
public interface OrderService {
    PageResponse<OrderDto> findAll(OrderFilterRequest requestFilter, PageableRequest pageableRequest);

    OrderDto reserve(Long id);

    OrderDto cancel(Long id);

    OrderDto buy(Long id);

    OrderDto findById(Long id);

    OrderDto update(Long id, OrderRequest t);

    OrderDto delete(Long id);

    OrderDto findByPostId(Long id);
}
