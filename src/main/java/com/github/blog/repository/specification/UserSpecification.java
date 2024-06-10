package com.github.blog.repository.specification;

import com.github.blog.model.Role;
import com.github.blog.model.Role_;
import com.github.blog.model.User;
import com.github.blog.model.UserInfo;
import com.github.blog.model.UserInfo_;
import com.github.blog.model.User_;
import com.github.blog.repository.filter.UserFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raman Haurylau
 */
public class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<User> filterBy(UserFilter userFilter) {
        return (root, query, cb) -> {
            Join<User, UserInfo> userInfo = root.join(User_.userInfo, JoinType.LEFT);
            Join<User, Role> role = root.join(User_.roles, JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (!ObjectUtils.isEmpty(userFilter.getFirstname())) {
                predicates.add(cb.like(cb.lower(userInfo.get(UserInfo_.firstname)), userFilter.getFirstname().toLowerCase().concat("%")));
            }

            if (!ObjectUtils.isEmpty(userFilter.getSurname())) {
                predicates.add(cb.like(cb.lower(userInfo.get(UserInfo_.surname)), userFilter.getSurname().toLowerCase().concat("%")));
            }

            if (!ObjectUtils.isEmpty(userFilter.getUniversity())) {
                predicates.add(cb.like(cb.lower(userInfo.get(UserInfo_.university)), userFilter.getUniversity().toLowerCase().concat("%")));
            }

            if (!ObjectUtils.isEmpty(userFilter.getMajor())) {
                predicates.add(cb.like(cb.lower(userInfo.get(UserInfo_.major)), userFilter.getMajor().toLowerCase().concat("%")));
            }

            if (!ObjectUtils.isEmpty(userFilter.getCompany())) {
                predicates.add(cb.like(cb.lower(userInfo.get(UserInfo_.company)), userFilter.getCompany().toLowerCase().concat("%")));
            }

            if (!ObjectUtils.isEmpty(userFilter.getJob())) {
                predicates.add(cb.like(cb.lower(userInfo.get(UserInfo_.job)), userFilter.getJob().toLowerCase().concat("%")));
            }

            if (!ObjectUtils.isEmpty(userFilter.getUsername())) {
                predicates.add(cb.like(cb.lower(root.get(User_.username)), userFilter.getUsername().toLowerCase().concat("%")));
            }

            if (!ObjectUtils.isEmpty(userFilter.getRoleId())) {
                predicates.add(cb.equal(role.get(Role_.id), userFilter.getRoleId()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
