package com.github.blog.repository.impl;

import com.github.blog.model.UserInfo;
import com.github.blog.model.UserInfo_;
import com.github.blog.repository.UserInfoDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.UserInfoFilter;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class UserInfoDaoImpl extends AbstractJpaDao<UserInfo, Long> implements UserInfoDao {

    @Override
    public Page<UserInfo> findAll(UserInfoFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserInfoBox> cq = cb.createQuery(UserInfoBox.class);
        Root<UserInfo> root = cq.from(UserInfo.class);

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(filter.getUserId())) {
            predicates.add(cb.equal(root.get(UserInfo_.id), filter.getUserId()));
        }

        cq.multiselect(root).distinct(true).where(cb.and(predicates.toArray(Predicate[]::new)));

        if (pageable.getOrderBy().equalsIgnoreCase("asc")) {
            cq.orderBy(cb.asc(root.get(UserInfo_.id)));
        } else {
            cq.orderBy(cb.desc(root.get(UserInfo_.id)));
        }

        TypedQuery<UserInfoBox> query = entityManager.createQuery(cq);

        int offset = Long.valueOf(pageable.getOffset()).intValue();

        if (offset < 0) {
            throw new RuntimeException("first-result value should be positive");
        }

        query.setFirstResult(offset);
        query.setMaxResults(pageable.getPageSize());

        List<UserInfo> results = query.getResultList().stream().map(UserInfoBox::getEntity).toList();

        long count;

        if (results.size() < pageable.getPageSize()) {
            count = results.size();
        } else {
            cq.orderBy(Collections.emptyList());
            cq.multiselect(cb.countDistinct(root.get(UserInfo_.id)));
            count = entityManager.createQuery(cq).getSingleResult().getCount();
        }

        return new Page<>(results, pageable, count);
    }
}

@Getter
class UserInfoBox {
    long count;
    UserInfo entity;

    UserInfoBox(long c) {
        count = c;
    }

    UserInfoBox(UserInfo e) {
        entity = e;
    }
}