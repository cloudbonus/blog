package com.github.blog.repository.impl;

import com.github.blog.model.Order;
import com.github.blog.model.Order_;
import com.github.blog.model.Post;
import com.github.blog.model.Post_;
import com.github.blog.model.Tag;
import com.github.blog.model.Tag_;
import com.github.blog.model.User;
import com.github.blog.model.User_;
import com.github.blog.repository.PostDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.PostFilter;
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
public class PostDaoImpl extends AbstractJpaDao<Post, Long> implements PostDao {

    @Override
    public Page<Post> findAll(PostFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<PostBox> cq = cb.createQuery(PostBox.class);
        Root<Post> root = cq.from(Post.class);
        Join<Post, User> user = root.join(Post_.user, JoinType.LEFT);
        Join<Post, Tag> tag = root.join(Post_.tags, JoinType.LEFT);
        Join<Post, Order> order = root.join(Post_.order, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(filter.getUsername())) {
            predicates.add(cb.like(cb.lower(user.get(User_.username).as(String.class)), filter.getUsername().toLowerCase()));
        }

        if (!ObjectUtils.isEmpty(filter.getState())) {
            predicates.add(cb.or(
                    cb.isNull(order.get(Order_.state)),
                    cb.equal(cb.lower(order.get(Order_.state).as(String.class)), filter.getState().toLowerCase())
            ));
        }

        if (!ObjectUtils.isEmpty(filter.getTagId())) {
            predicates.add(cb.equal(tag.get(Tag_.id), filter.getTagId()));
        }

        cq.multiselect(root).distinct(true).where(cb.and(predicates.toArray(Predicate[]::new)));

        if (pageable.getOrderBy().equalsIgnoreCase("asc")) {
            cq.orderBy(cb.asc(root.get(Post_.id)));
        } else {
            cq.orderBy(cb.desc(root.get(Post_.id)));
        }

        TypedQuery<PostBox> query = entityManager.createQuery(cq);

        int offset = Long.valueOf(pageable.getOffset()).intValue();

        if (offset < 0) {
            throw new RuntimeException("first-result value should be positive");
        }

        query.setFirstResult(offset);
        query.setMaxResults(pageable.getPageSize());

        List<Post> results = query.getResultList().stream().map(PostBox::getEntity).toList();

        long count;

        if (results.size() < pageable.getPageSize()) {
            count = results.size();
        } else {
            cq.orderBy(Collections.emptyList());
            cq.multiselect(cb.countDistinct(root.get(Post_.id)));
            count = entityManager.createQuery(cq).getSingleResult().getCount();
        }
        return new Page<>(results, pageable, count);
    }
}

@Getter
class PostBox {
    long count;
    Post entity;

    PostBox(long c) {
        count = c;
    }

    PostBox(Post e) {
        entity = e;
    }
}
