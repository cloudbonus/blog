package com.github.blog.repository.specification;

import com.github.blog.model.Role;
import com.github.blog.model.Role_;
import com.github.blog.model.User;
import com.github.blog.model.User_;
import com.github.blog.repository.filter.RoleFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

/**
 * @author Raman Haurylau
 */
public class RoleSpecification {

    private RoleSpecification() {
    }

    public static Specification<Role> filterBy(RoleFilter roleFilter) {
        return Specification
                .where(hasUserId(roleFilter.getUserId()));
    }

    private static Specification<Role> hasUserId(Long roleId) {
        return (root, query, cb) -> {
            if (!ObjectUtils.isEmpty(roleId)) {
                Join<Role, User> user = root.join(Role_.users, JoinType.LEFT);
                return cb.equal(user.get(User_.id), roleId);
            } else {
                return cb.conjunction();
            }
        };
    }
}
