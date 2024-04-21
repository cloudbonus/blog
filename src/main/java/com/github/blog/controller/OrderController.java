package com.github.blog.controller;

import com.github.blog.controller.mapper.JsonMapper;
import com.github.blog.dto.OrderDto;
import com.github.blog.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final JsonMapper jsonMapper;

    public String create(OrderDto orderDto) {
        return jsonMapper.convertToJson(orderService.create(orderDto));
    }

    public String findById(Long id) {
        return jsonMapper.convertToJson(orderService.findById(id));
    }

    public String findAll() {
        List<OrderDto> orders = orderService.findAll();
        return jsonMapper.convertToJson(orders);
    }

    public OrderDto update(Long id, OrderDto orderDto) {
        return orderService.update(id, orderDto);
    }

    public void delete(Long id) {
        orderService.delete(id);
    }
}

