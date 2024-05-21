package com.github.blog.controller;

import com.github.blog.controller.dto.common.OrderDto;
import com.github.blog.controller.dto.request.OrderRequest;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.filter.OrderDtoFilter;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Raman Haurylau
 */
@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('COMPANY') and #id == authentication.principal.id)")
    public OrderDto reserve(@RequestBody @P("id") Long id) {
        return orderService.reserve(id);
    }

    @GetMapping("{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('COMPANY') and #id == authentication.principal.id)")
    public OrderDto cancel(@PathVariable("id") @P("id") Long id) {
        return orderService.cancel(id);
    }

    @GetMapping("{id}/buy")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('COMPANY') and #id == authentication.principal.id)")
    public OrderDto buy(@PathVariable("id") @P("id") Long id) {
        return orderService.buy(id);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('COMPANY') and #id == authentication.principal.id)")
    public OrderDto findById(@PathVariable("id") Long id) {
        return orderService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('COMPANY') and @orderAccess.canFilter(#filter.userId))")
    public Page<OrderDto> findAll(@P("filter") OrderDtoFilter filter, PageableRequest pageableRequest) {
        return orderService.findAll(filter, pageableRequest);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDto update(@PathVariable("id") Long id, @RequestBody OrderRequest orderDto) {
        return orderService.update(id, orderDto);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDto delete(@PathVariable("id") Long id) {
        return orderService.findById(id);
    }
}

