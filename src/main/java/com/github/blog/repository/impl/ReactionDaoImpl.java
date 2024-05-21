package com.github.blog.repository.impl;

import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Reaction;
import com.github.blog.model.Reaction_;
import com.github.blog.repository.ReactionDao;
import com.github.blog.repository.dto.common.Pageable;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Repository
public class ReactionDaoImpl extends AbstractJpaDao<Reaction, Long> implements ReactionDao {
    @Override
    @Transactional
    public Page<Reaction> findAll(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ReactionBox> cq = cb.createQuery(ReactionBox.class);
        Root<Reaction> root = cq.from(Reaction.class);

        cq.multiselect(root).distinct(true);

        if (pageable.getOrderBy().equalsIgnoreCase("asc")) {
            cq.orderBy(cb.asc(root.get(Reaction_.id)));
        } else {
            cq.orderBy(cb.desc(root.get(Reaction_.id)));
        }

        TypedQuery<ReactionBox> query = entityManager.createQuery(cq);

        int offset = Long.valueOf(pageable.getOffset()).intValue();

        if (offset < 0) {
            throw new RuntimeException("first-result value should be positive");
        }

        query.setFirstResult(offset);
        query.setMaxResults(pageable.getPageSize());

        List<Reaction> results = query.getResultList().stream().map(ReactionBox::getEntity).toList();

        long count;

        if (results.size() < pageable.getPageSize()) {
            count = results.size();
        } else {
            cq.orderBy(Collections.emptyList());
            cq.multiselect(cb.countDistinct(root.get(Reaction_.id)));
            count = entityManager.createQuery(cq).getSingleResult().getCount();
        }

        return new Page<>(results, pageable, count);
    }
}

@Getter
class ReactionBox {
    long count;
    Reaction entity;

    ReactionBox(long c) {
        count = c;
    }

    ReactionBox(Reaction e) {
        entity = e;
    }
}
