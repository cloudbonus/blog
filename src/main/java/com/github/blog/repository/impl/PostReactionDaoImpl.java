package com.github.blog.repository.impl;

import com.github.blog.model.Post;
import com.github.blog.model.PostReaction;
import com.github.blog.model.PostReaction_;
import com.github.blog.model.Post_;
import com.github.blog.model.Reaction;
import com.github.blog.model.Reaction_;
import com.github.blog.model.User;
import com.github.blog.model.User_;
import com.github.blog.repository.PostReactionDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.PostReactionFilter;
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
public class PostReactionDaoImpl extends AbstractJpaDao<PostReaction, Long> implements PostReactionDao {

    @Override
    public Page<PostReaction> findAll(PostReactionFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostReactionBox> cq = cb.createQuery(PostReactionBox.class);
        Root<PostReaction> root = cq.from(PostReaction.class);
        Join<PostReaction, Reaction> reaction = root.join(PostReaction_.reaction, JoinType.LEFT);
        Join<PostReaction, User> user = root.join(PostReaction_.user, JoinType.LEFT);
        Join<PostReaction, Post> post = root.join(PostReaction_.post, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(filter.getPostId())) {
            predicates.add(cb.equal(post.get(Post_.id), filter.getPostId()));
        }

        if (!ObjectUtils.isEmpty(filter.getUsername())) {
            predicates.add(cb.like(cb.lower(user.get(User_.username).as(String.class)), filter.getUsername().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getReactionId())) {
            predicates.add(cb.equal(reaction.get(Reaction_.reactionName), filter.getReactionId()));
        }

        cq.multiselect(root).distinct(true).where(cb.and(predicates.toArray(Predicate[]::new)));

        if (pageable.getOrderBy().equalsIgnoreCase("asc")) {
            cq.orderBy(cb.asc(root.get(PostReaction_.id)));
        } else {
            cq.orderBy(cb.desc(root.get(PostReaction_.id)));
        }

        TypedQuery<PostReactionBox> query = entityManager.createQuery(cq);

        int offset = Long.valueOf(pageable.getOffset()).intValue();

        if (offset < 0) {
            throw new RuntimeException("first-result value should be positive");
        }

        query.setFirstResult(offset);
        query.setMaxResults(pageable.getPageSize());

        List<PostReaction> results = query.getResultList().stream().map(PostReactionBox::getEntity).toList();

        long count;

        if (results.size() < pageable.getPageSize()) {
            count = results.size();
        } else {
            cq.orderBy(Collections.emptyList());
            cq.multiselect(cb.countDistinct(root.get(PostReaction_.id)));
            count = entityManager.createQuery(cq).getSingleResult().getCount();
        }

        return new Page<>(results, pageable, count);
    }

    @Override
    public Optional<PostReaction> findByPostIdAndUserId(Long postId, Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostReaction> cq = cb.createQuery(PostReaction.class);
        Root<PostReaction> root = cq.from(PostReaction.class);
        Join<PostReaction, Post> post = root.join(PostReaction_.post, JoinType.LEFT);
        Join<PostReaction, User> user = root.join(PostReaction_.user, JoinType.LEFT);

        cq.select(root).where(cb.equal(post.get(Post_.id), postId)).where(cb.equal(user.get(User_.id), userId));
        TypedQuery<PostReaction> query = entityManager.createQuery(cq);

        List<PostReaction> result = query.getResultList();

        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return result.stream().findFirst();
        }
    }
}

@Getter
class PostReactionBox {
    long count;
    PostReaction entity;

    PostReactionBox(long c) {
        count = c;
    }

    PostReactionBox(PostReaction e) {
        entity = e;
    }
}


