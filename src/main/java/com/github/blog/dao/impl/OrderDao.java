package com.github.blog.dao.impl;

import com.github.blog.dao.Dao;
import com.github.blog.model.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class OrderDao implements Dao<Order> {
    private final List<Order> orders = new ArrayList<>();

//    {
//        orders = new ArrayList<>();
//        orders.add(new Order(++ORDER_COUNT, "posuere felis sed lacus morbi sem mauris laoreet ut rhoncus aliquet pulvinar", "complete", LocalDateTime.now(), 1, 1));
//        orders.add(new Order(++ORDER_COUNT, "interdum venenatis turpis enim blandit mi in porttitor pede justo eu massa donec dapibus duis", "cancelled", LocalDateTime.now(), 2, 1));
//        orders.add(new Order(++ORDER_COUNT, "nisl ut volutpat sapien arcu sed augue aliquam erat volutpat in congue etiam justo etiam pretium iaculis justo in", "pending", LocalDateTime.now(), 3, 2));
//    }

    @Override
    public Optional<Order> getById(int id) {
        return orders.stream().filter(o -> o.getOrderId() == id).findAny();
    }

    @Override
    public List<Order> getAll() {
        return orders;
    }

    @Override
    public int save(Order order) {
        orders.add(order);
        int index = orders.size();
        order.setOrderId(index);
        return index;
    }

    @Override
    public boolean update(Order order) {
        if (orders.stream().map(o -> o.getOrderId() == order.getOrderId()).findAny().orElse(false)) {
            orders.set(order.getOrderId() - 1, order);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return orders.removeIf(o -> o.getOrderId() == id);
    }
}
