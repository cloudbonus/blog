package com.github.blog.repository.impl;

import com.github.blog.model.Order;
import com.github.blog.model.Order_;
import com.github.blog.model.Post_;
import com.github.blog.model.User;
import com.github.blog.model.User_;
import com.github.blog.repository.OrderDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.OrderFilter;
import com.github.blog.service.statemachine.state.OrderState;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Repository
@Transactional
public class OrderDaoImpl extends AbstractJpaDao<Order, Long> implements OrderDao {

    @Override
    public Page<Order> findAll(OrderFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<OrderBox> cq = cb.createQuery(OrderBox.class);
        Root<Order> root = cq.from(Order.class);
        Join<Order, User> user = root.join(Order_.user, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(filter.getUsername())) {
            predicates.add(cb.like(cb.lower(user.get(User_.username).as(String.class)), filter.getUsername().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getUserId())) {
            predicates.add(cb.equal(root.get(Order_.user).get(User_.id).as(Long.class), filter.getUserId()));
        }

        if (!ObjectUtils.isEmpty(filter.getPostId())) {
            predicates.add(cb.equal(root.get(Order_.post).get(Post_.id).as(Long.class), filter.getPostId()));
        }

        cq.multiselect(root).distinct(true).where(cb.and(predicates.toArray(Predicate[]::new)));

        if (pageable.getOrderBy().equalsIgnoreCase("asc")) {
            cq.orderBy(cb.asc(root.get(Order_.id)));
        } else {
            cq.orderBy(cb.desc(root.get(Order_.id)));
        }

        TypedQuery<OrderBox> query = entityManager.createQuery(cq);

        int offset = Long.valueOf(pageable.getOffset()).intValue();

        if (offset < 0) {
            throw new RuntimeException("first-result value should be positive");
        }

        query.setFirstResult(offset);
        query.setMaxResults(pageable.getPageSize());

        List<Order> results = query.getResultList().stream().map(OrderBox::getEntity).toList();

        long count;

        if (results.size() < pageable.getPageSize()) {
            count = results.size();
        } else {
            cq.orderBy(Collections.emptyList());
            cq.multiselect(cb.countDistinct(root.get(Order_.id)));
            count = entityManager.createQuery(cq).getSingleResult().getCount();
        }
        return new Page<>(results, pageable, count);
    }

    @Override
    public List<Order> findAllInactiveOrders() {
        TypedQuery<Order> query = entityManager.createQuery("select o from Order o where o.state = :stateOne or o.state = :stateTwo", Order.class);
        query.setParameter("stateOne", OrderState.CANCELED.name());
        query.setParameter("stateTwo", OrderState.NEW.name());
        return query.getResultList();
    }

    @Override
    public Optional<Order> findByPostId(Long id) {
        TypedQuery<Order> query = entityManager.createQuery("select o from Order o where o.post.id = :id", Order.class);
        query.setParameter("id", id);
        List<Order> result = query.getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else return result.stream().findFirst();
    }
}

@Getter
class OrderBox {
    long count;
    Order entity;

    OrderBox(long c) {
        count = c;
    }

    OrderBox(Order e) {
        entity = e;
    }
}
