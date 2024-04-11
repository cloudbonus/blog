package com.github.blog.controller;

import com.github.blog.dto.OrderDto;
import com.github.blog.service.OrderService;
import com.github.blog.util.DefaultMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final DefaultMapper mapper;

    public String create(OrderDto orderDto) {
        return mapper.convertToJson(orderService.create(orderDto));
    }

    public String findById(int id) {
        return mapper.convertToJson(orderService.findById(id));
    }

    public String findAll() {
        List<OrderDto> orders = orderService.findAll();
        return mapper.convertToJson(orders);
    }

    public OrderDto update(int id, OrderDto orderDto) {
        return orderService.update(id, orderDto);
    }

    public String remove(int id) {
        int result = orderService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
    }

}

