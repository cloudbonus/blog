package com.github.blog.repository.specification;

import com.github.blog.repository.entity.UserInfo;
import com.github.blog.repository.entity.UserInfo_;
import com.github.blog.repository.filter.UserInfoFilter;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Raman Haurylau
 */
public class UserInfoSpecification {

    private UserInfoSpecification() {
    }

    public static Specification<UserInfo> filterBy(UserInfoFilter userInfoFilter) {
        return Specification
                .where(hasUserId(userInfoFilter.getUserId()));
    }

    private static Specification<UserInfo> hasUserId(Long userId) {
        return (root, query, cb) -> userId == null ? cb.conjunction() : cb.equal(root.get(UserInfo_.id), userId);
    }
}
