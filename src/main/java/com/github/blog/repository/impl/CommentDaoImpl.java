package com.github.blog.repository.impl;

import com.github.blog.controller.dto.response.Page;
import com.github.blog.controller.dto.response.Pageable;
import com.github.blog.model.Comment;
import com.github.blog.model.Comment_;
import com.github.blog.model.User;
import com.github.blog.model.User_;
import com.github.blog.repository.CommentDao;
import com.github.blog.repository.dto.filter.CommentFilter;
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
public class CommentDaoImpl extends AbstractJpaDao<Comment, Long> implements CommentDao {

    @Override
    public Page<Comment> findAll(CommentFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<CommentBox> cq = cb.createQuery(CommentBox.class);
        Root<Comment> root = cq.from(Comment.class);
        Join<Comment, User> user = root.join(Comment_.user, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(filter.getLogin())) {
            predicates.add(cb.equal(cb.lower(user.get(User_.login).as(String.class)), filter.getLogin().toLowerCase()));
        }

        cq.multiselect(root).distinct(true).where(cb.and(predicates.toArray(Predicate[]::new)));
        cq.orderBy(cb.asc(root.get(Comment_.id)));

        TypedQuery<CommentBox> query = entityManager.createQuery(cq);

        Pageable pageable = new Pageable();
        pageable.setPageNumber(filter.getPageNumber());
        pageable.setPageSize(filter.getPageSize());

        int offset = Long.valueOf(pageable.getOffset()).intValue();

        if (offset < 0) {
            throw new RuntimeException("first-result value should be positive");
        }

        query.setFirstResult(offset);
        query.setMaxResults(pageable.getPageSize());

        List<Comment> results = query.getResultList().stream().map(CommentBox::getEntity).toList();

        long count;

        if (results.size() < pageable.getPageSize()) {
            count = results.size();
        } else {
            cq.orderBy(Collections.emptyList());
            cq.multiselect(cb.countDistinct(root.get(Comment_.id)));
            count = entityManager.createQuery(cq).getSingleResult().getCount();
        }

        return new Page<>(results, pageable, count);
    }
}

@Getter
class CommentBox {
    long count;
    Comment entity;

    CommentBox(long c) {
        count = c;
    }

    CommentBox(Comment e) {
        entity = e;
    }
}
