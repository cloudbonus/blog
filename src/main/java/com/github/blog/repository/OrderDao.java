package com.github.blog.repository;

import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Order;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.OrderFilter;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface OrderDao extends CrudDao<Order, Long> {
    Page<Order> findAll(OrderFilter filter, Pageable pageable);

    List<Order> findAllInactiveOrders();

    Optional<Order> findByPostId(final Long aLong);
}
