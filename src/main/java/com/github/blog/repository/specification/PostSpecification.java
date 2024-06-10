package com.github.blog.repository.specification;

import com.github.blog.model.Order;
import com.github.blog.model.Order_;
import com.github.blog.model.Post;
import com.github.blog.model.Post_;
import com.github.blog.model.Tag;
import com.github.blog.model.Tag_;
import com.github.blog.model.User;
import com.github.blog.model.User_;
import com.github.blog.repository.filter.PostFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

/**
 * @author Raman Haurylau
 */
public class PostSpecification {

    private PostSpecification() {
    }

    public static Specification<Post> filterBy(PostFilter postFilter) {
        return Specification
                .where(hasUsername(postFilter.getUsername()))
                .and(hasState(postFilter.getState()))
                .and(hasTagId(postFilter.getTagId()));
    }

    private static Specification<Post> hasTagId(Long tagId) {
        return (root, query, cb) -> {
            if (!ObjectUtils.isEmpty(tagId)) {
                Join<Post, Tag> tag = root.join(Post_.tags, JoinType.LEFT);
                return cb.equal(tag.get(Tag_.id), tagId);
            } else {
                return cb.conjunction();
            }
        };
    }

    private static Specification<Post> hasState(String state) {
        return (root, query, cb) -> {
            if (!ObjectUtils.isEmpty(state)) {
                Join<Post, Order> order = root.join(Post_.order, JoinType.LEFT);
                return cb.or(
                        cb.isNull(order.get(Order_.state)),
                        cb.equal(cb.lower(order.get(Order_.state).as(String.class)), state.toLowerCase())
                );
            } else {
                return cb.conjunction();
            }
        };
    }

    private static Specification<Post> hasUsername(String username) {
        return (root, query, cb) -> {
            if (!ObjectUtils.isEmpty(username)) {
                Join<Post, User> user = root.join(Post_.user, JoinType.LEFT);
                return cb.like(cb.lower(user.get(User_.username)), username.toLowerCase().concat("%"));
            } else {
                return cb.conjunction();
            }
        };
    }
}
