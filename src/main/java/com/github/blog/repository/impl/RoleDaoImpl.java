package com.github.blog.repository.impl;

import com.github.blog.model.Role;
import com.github.blog.model.Role_;
import com.github.blog.model.User;
import com.github.blog.model.User_;
import com.github.blog.repository.RoleDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.RoleFilter;
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
public class RoleDaoImpl extends AbstractJpaDao<Role, Long> implements RoleDao {

    private static final String DEFAULT_ORDER = "asc";

    @Override
    public Page<Role> findAll(RoleFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoleBox> cq = cb.createQuery(RoleBox.class);
        Root<Role> root = cq.from(Role.class);
        Join<Role, User> user = root.join(Role_.users, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(filter.getUserId())) {
            predicates.add(cb.equal(user.get(User_.id), filter.getUserId()));
        }

        cq.multiselect(root).distinct(true).where(cb.and(predicates.toArray(Predicate[]::new)));

        if (pageable.getOrderBy().equalsIgnoreCase(DEFAULT_ORDER)) {
            cq.orderBy(cb.asc(root.get(Role_.id)));
        } else {
            cq.orderBy(cb.desc(root.get(Role_.id)));
        }

        TypedQuery<RoleBox> query = entityManager.createQuery(cq);

        int offset = Long.valueOf(pageable.getOffset()).intValue();

        if (offset < 0) {
            throw new RuntimeException("first-result value should be positive");
        }

        query.setFirstResult(offset);
        query.setMaxResults(pageable.getPageSize());

        List<Role> results = query.getResultList().stream().map(RoleBox::getEntity).toList();

        long count;

        if (results.size() < pageable.getPageSize()) {
            count = results.size();
        } else {
            cq.orderBy(Collections.emptyList());
            cq.multiselect(cb.countDistinct(root.get(Role_.id)));
            count = entityManager.createQuery(cq).getSingleResult().getCount();
        }

        return new Page<>(results, pageable, count);
    }

    @Override
    public Optional<Role> findByName(String name) {
        TypedQuery<Role> query = entityManager.createQuery("select r from Role r where r.name = :name", Role.class);
        query.setParameter("name", name);
        List<Role> result = query.getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return result.stream().findFirst();
        }
    }
}

@Getter
class RoleBox {
    long count;
    Role entity;

    RoleBox(long c) {
        count = c;
    }

    RoleBox(Role e) {
        entity = e;
    }
}

