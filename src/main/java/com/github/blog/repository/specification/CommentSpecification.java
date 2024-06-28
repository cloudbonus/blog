package com.github.blog.repository.specification;

import com.github.blog.repository.entity.Comment;
import com.github.blog.repository.entity.Comment_;
import com.github.blog.repository.entity.User;
import com.github.blog.repository.entity.User_;
import com.github.blog.repository.filter.CommentFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

/**
 * @author Raman Haurylau
 */
public class CommentSpecification {

    private CommentSpecification() {
    }

    public static Specification<Comment> filterBy(CommentFilter commentFilter) {
        return Specification
                .where(hasUsername(commentFilter.getUsername()));
    }

    private static Specification<Comment> hasUsername(String username) {
        return (root, query, cb) -> {
            if (!ObjectUtils.isEmpty(username)) {
                Join<Comment, User> user = root.join(Comment_.user, JoinType.LEFT);
                return cb.like(cb.lower(user.get(User_.username)), username.toLowerCase().concat("%"));
            } else {
                return cb.conjunction();
            }
        };
    }
}
