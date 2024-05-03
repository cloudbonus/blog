package com.github.blog.repository.impl;

import com.github.blog.model.Order;
import com.github.blog.repository.OrderDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Repository
@Transactional
public class OrderDaoImpl extends AbstractJpaDao<Order, Long> implements OrderDao {
}
