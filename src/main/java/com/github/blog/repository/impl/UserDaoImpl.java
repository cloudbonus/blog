package com.github.blog.repository.impl;

import com.github.blog.controller.dto.response.Page;
import com.github.blog.controller.dto.response.Pageable;
import com.github.blog.model.Role;
import com.github.blog.model.Role_;
import com.github.blog.model.User;
import com.github.blog.model.UserDetail;
import com.github.blog.model.UserDetail_;
import com.github.blog.model.User_;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.filter.UserFilter;
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

/**
 * @author Raman Haurylau
 */
@Repository
@Transactional
public class UserDaoImpl extends AbstractJpaDao<User, Long> implements UserDao {

    @Override
    public Page<User> findAll(UserFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<UserBox> cq = cb.createQuery(UserBox.class);
        Root<User> root = cq.from(User.class);
        Join<User, UserDetail> userDetail = root.join(User_.userDetail, JoinType.LEFT);
        Join<User, Role> role = root.join(User_.roles, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(filter.getFirstname())) {
            predicates.add(cb.like(cb.lower(userDetail.get(UserDetail_.firstname).as(String.class)), filter.getFirstname().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getSurname())) {
            predicates.add(cb.like(cb.lower(userDetail.get(UserDetail_.surname).as(String.class)), filter.getSurname().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getUniversity())) {
            predicates.add(cb.like(cb.lower(userDetail.get(UserDetail_.universityName).as(String.class)), filter.getUniversity().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getMajor())) {
            predicates.add(cb.like(cb.lower(userDetail.get(UserDetail_.majorName).as(String.class)), filter.getMajor().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getCompany())) {
            predicates.add(cb.like(cb.lower(userDetail.get(UserDetail_.companyName).as(String.class)), filter.getCompany().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getJob())) {
            predicates.add(cb.like(cb.lower(userDetail.get(UserDetail_.jobTitle).as(String.class)), filter.getJob().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getLogin())) {
            predicates.add(cb.equal(cb.lower(root.get(User_.login).as(String.class)), filter.getLogin().toLowerCase()));
        }

        if (!ObjectUtils.isEmpty(filter.getRole())) {
            String updatedRoleName = "role_" + filter.getRole();
            predicates.add(cb.equal(cb.lower(role.get(Role_.roleName).as(String.class)), updatedRoleName.toLowerCase()));
        }

        cq.multiselect(root).distinct(true).where(cb.and(predicates.toArray(Predicate[]::new)));
        cq.orderBy(cb.asc(root.get(User_.id)));

        TypedQuery<UserBox> query = entityManager.createQuery(cq);

        Pageable pageable = new Pageable();
        pageable.setPageNumber(filter.getPageNumber());
        pageable.setPageSize(filter.getPageSize());

        int offset = Long.valueOf(pageable.getOffset()).intValue();

        if (offset < 0) {
            throw new RuntimeException("first-result value should be positive");
        }

        query.setFirstResult(offset);
        query.setMaxResults(pageable.getPageSize());

        List<User> results = query.getResultList().stream().map(UserBox::getEntity).toList();

        long count;

        if (results.size() < pageable.getPageSize()) {
            count = results.size();
        } else {
            cq.orderBy(Collections.emptyList());
            cq.multiselect(cb.countDistinct(root.get(User_.id)));
            count = entityManager.createQuery(cq).getSingleResult().getCount();
        }

        return new Page<>(results, pageable, count);
    }
}

@Getter
class UserBox {
    long count;
    User entity;

    UserBox(long c) {
        count = c;
    }

    UserBox(User e) {
        entity = e;
    }
}