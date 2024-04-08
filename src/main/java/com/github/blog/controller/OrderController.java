package com.github.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dto.OrderDto;
import com.github.blog.service.OrderService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
public class OrderController {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderController(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    public String create(OrderDto orderDto) {
        return convertToJson(orderService.create(orderDto));
    }

    public String findById(int id) {
        return convertToJson(orderService.findById(id));
    }

    public String findAll() {
        List<OrderDto> orders = orderService.findAll();
        return convertToJsonArray(orders);
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

    @SneakyThrows
    private String convertToJson(OrderDto orderDto) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orderDto);
    }

    @SneakyThrows
    private String convertToJsonArray(List<OrderDto> orders) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orders);
    }
}

