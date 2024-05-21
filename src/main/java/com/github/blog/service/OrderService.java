package com.github.blog.service;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.OrderRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.OrderDtoFilter;
import com.github.blog.controller.dto.response.Page;


/**
 * @author Raman Haurylau
 */
public interface OrderService {
    Page<OrderDto> findAll(OrderDtoFilter requestFilter, PageableRequest pageableRequest);

    OrderDto reserve(Long id);

    OrderDto cancel(Long id);

    OrderDto buy(Long id);

    OrderDto findById(Long id);

    OrderDto update(Long id, OrderRequest t);

    OrderDto delete(Long id);

    OrderDto findByPostId(Long id);
}
