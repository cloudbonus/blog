package com.github.blog.repository.impl;

import com.github.blog.model.Role;
import com.github.blog.model.Role_;
import com.github.blog.model.User;
import com.github.blog.model.UserInfo;
import com.github.blog.model.UserInfo_;
import com.github.blog.model.User_;
import com.github.blog.repository.UserDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
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
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Repository
@Transactional
public class UserDaoImpl extends AbstractJpaDao<User, Long> implements UserDao {

    private static final String DEFAULT_ORDER = "asc";

    @Override
    public Page<User> findAll(UserFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<UserBox> cq = cb.createQuery(UserBox.class);
        Root<User> root = cq.from(User.class);
        Join<User, UserInfo> userInfo = root.join(User_.userInfo, JoinType.LEFT);
        Join<User, Role> role = root.join(User_.roles, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(filter.getFirstname())) {
            predicates.add(cb.like(cb.lower(userInfo.get(UserInfo_.firstname).as(String.class)), filter.getFirstname().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getSurname())) {
            predicates.add(cb.like(cb.lower(userInfo.get(UserInfo_.surname).as(String.class)), filter.getSurname().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getUniversity())) {
            predicates.add(cb.like(cb.lower(userInfo.get(UserInfo_.university).as(String.class)), filter.getUniversity().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getMajor())) {
            predicates.add(cb.like(cb.lower(userInfo.get(UserInfo_.major).as(String.class)), filter.getMajor().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getCompany())) {
            predicates.add(cb.like(cb.lower(userInfo.get(UserInfo_.company).as(String.class)), filter.getCompany().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getJob())) {
            predicates.add(cb.like(cb.lower(userInfo.get(UserInfo_.job).as(String.class)), filter.getJob().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getUsername())) {
            predicates.add(cb.like(cb.lower(root.get(User_.username).as(String.class)), filter.getUsername().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getRoleId())) {
            predicates.add(cb.equal(role.get(Role_.id), filter.getRoleId()));
        }

        cq.multiselect(root).distinct(true).where(cb.and(predicates.toArray(Predicate[]::new)));

        if (pageable.getOrderBy().equalsIgnoreCase(DEFAULT_ORDER)) {
            cq.orderBy(cb.asc(root.get(User_.id)));
        } else {
            cq.orderBy(cb.desc(root.get(User_.id)));
        }

        TypedQuery<UserBox> query = entityManager.createQuery(cq);

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

    @Override
    public Optional<User> findByUsername(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        root.fetch(User_.roles, JoinType.LEFT);

        cq.select(root).where(cb.like(cb.lower(root.get(User_.username).as(String.class)), username.toLowerCase()));
        TypedQuery<User> query = entityManager.createQuery(cq);

        List<User> result = query.getResultList();

        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return result.stream().findFirst();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        root.fetch(User_.roles, JoinType.LEFT);

        cq.select(root).where(cb.like(cb.lower(root.get(User_.email).as(String.class)), email.toLowerCase()));
        TypedQuery<User> query = entityManager.createQuery(cq);

        List<User> result = query.getResultList();

        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return result.stream().findFirst();
        }
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