package com.github.blog.controller;

import com.github.blog.dto.common.OrderDto;
import com.github.blog.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderDto create(@RequestBody OrderDto orderDto) {
        return orderService.create(orderDto);
    }

    @GetMapping("{id}")
    public OrderDto findById(@PathVariable("id") Long id) {
        return orderService.findById(id);
    }

    @GetMapping
    public List<OrderDto> findAll() {
        return orderService.findAll();
    }

    @PutMapping("{id}")
    public OrderDto update(@PathVariable("id") Long id, @RequestBody OrderDto orderDto) {
        return orderService.update(id, orderDto);
    }

    @DeleteMapping("{id}")
    public OrderDto delete(@PathVariable("id") Long id) {
        return orderService.findById(id);
    }
}

