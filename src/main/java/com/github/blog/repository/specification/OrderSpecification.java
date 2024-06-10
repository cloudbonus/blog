package com.github.blog.repository.specification;

import com.github.blog.model.Order;
import com.github.blog.model.Order_;
import com.github.blog.model.Post_;
import com.github.blog.model.User;
import com.github.blog.model.User_;
import com.github.blog.repository.filter.OrderFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

/**
 * @author Raman Haurylau
 */
public class OrderSpecification {

    private OrderSpecification() {
    }

    public static Specification<Order> filterBy(OrderFilter orderFilter) {
        return Specification
                .where(hasUsername(orderFilter.getUsername()))
                .and(hasUserId(orderFilter.getUserId()))
                .and(hasPostId(orderFilter.getPostId()));
    }

    private static Specification<Order> hasUserId(Long userId) {
        return (root, query, cb) -> userId == null ? cb.conjunction() : cb.equal(root.get(Order_.user).get(User_.id), userId);
    }

    private static Specification<Order> hasPostId(Long postId) {
        return (root, query, cb) -> postId == null ? cb.conjunction() : cb.equal(root.get(Order_.post).get(Post_.id), postId);
    }

    private static Specification<Order> hasUsername(String username) {
        return (root, query, cb) -> {
            if (!ObjectUtils.isEmpty(username)) {
                Join<Order, User> user = root.join(Order_.user, JoinType.LEFT);
                return cb.like(cb.lower(user.get(User_.username)), username.toLowerCase().concat("%"));
            } else {
                return cb.conjunction();
            }
        };
    }
}
