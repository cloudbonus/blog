package com.github.blog.repository.impl;

import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Comment;
import com.github.blog.model.CommentReaction;
import com.github.blog.model.CommentReaction_;
import com.github.blog.model.Comment_;
import com.github.blog.model.Reaction;
import com.github.blog.model.Reaction_;
import com.github.blog.model.User;
import com.github.blog.model.User_;
import com.github.blog.repository.CommentReactionDao;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.CommentReactionFilter;
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
public class CommentReactionDaoImpl extends AbstractJpaDao<CommentReaction, Long> implements CommentReactionDao {
    @Override
    @Transactional
    public Page<CommentReaction> findAll(CommentReactionFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CommentReactionBox> cq = cb.createQuery(CommentReactionBox.class);
        Root<CommentReaction> root = cq.from(CommentReaction.class);
        Join<CommentReaction, Reaction> reaction = root.join(CommentReaction_.reaction, JoinType.LEFT);
        Join<CommentReaction, User> user = root.join(CommentReaction_.user, JoinType.LEFT);
        Join<CommentReaction, Comment> comment = root.join(CommentReaction_.comment, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(filter.getCommentId())) {
            predicates.add(cb.equal(comment.get(Comment_.id), filter.getCommentId()));
        }

        if (!ObjectUtils.isEmpty(filter.getUsername())) {
            predicates.add(cb.like(cb.lower(user.get(User_.username).as(String.class)), filter.getUsername().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getReactionId())) {
            predicates.add(cb.equal(reaction.get(Reaction_.reactionName), filter.getReactionId()));
        }

        cq.multiselect(root).distinct(true).where(cb.and(predicates.toArray(Predicate[]::new)));

        if (pageable.getOrderBy().equalsIgnoreCase("asc")) {
            cq.orderBy(cb.asc(root.get(CommentReaction_.id)));
        } else {
            cq.orderBy(cb.desc(root.get(CommentReaction_.id)));
        }

        TypedQuery<CommentReactionBox> query = entityManager.createQuery(cq);

        int offset = Long.valueOf(pageable.getOffset()).intValue();

        if (offset < 0) {
            throw new RuntimeException("first-result value should be positive");
        }

        query.setFirstResult(offset);
        query.setMaxResults(pageable.getPageSize());

        List<CommentReaction> results = query.getResultList().stream().map(CommentReactionBox::getEntity).toList();

        long count;

        if (results.size() < pageable.getPageSize()) {
            count = results.size();
        } else {
            cq.orderBy(Collections.emptyList());
            cq.multiselect(cb.countDistinct(root.get(CommentReaction_.id)));
            count = entityManager.createQuery(cq).getSingleResult().getCount();
        }

        return new Page<>(results, pageable, count);
    }
}

@Getter
class CommentReactionBox {
    long count;
    CommentReaction entity;

    CommentReactionBox(long c) {
        count = c;
    }

    CommentReactionBox(CommentReaction e) {
        entity = e;
    }
}


