package com.github.blog.repository.impl;

import com.github.blog.model.Post;
import com.github.blog.model.Post_;
import com.github.blog.model.Tag;
import com.github.blog.model.Tag_;
import com.github.blog.repository.TagDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.TagFilter;
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
public class TagDaoImpl extends AbstractJpaDao<Tag, Long> implements TagDao {

    private static final String DEFAULT_ORDER = "asc";

    @Override
    public Page<Tag> findAll(TagFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TagBox> cq = cb.createQuery(TagBox.class);
        Root<Tag> root = cq.from(Tag.class);
        Join<Tag, Post> post = root.join(Tag_.posts, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(filter.getPostId())) {
            predicates.add(cb.equal(post.get(Post_.id), filter.getPostId()));
        }

        cq.multiselect(root).distinct(true).where(cb.and(predicates.toArray(Predicate[]::new)));

        if (pageable.getOrderBy().equalsIgnoreCase(DEFAULT_ORDER)) {
            cq.orderBy(cb.asc(root.get(Tag_.id)));
        } else {
            cq.orderBy(cb.desc(root.get(Tag_.id)));
        }

        TypedQuery<TagBox> query = entityManager.createQuery(cq);

        int offset = Long.valueOf(pageable.getOffset()).intValue();

        if (offset < 0) {
            throw new RuntimeException("first-result value should be positive");
        }

        query.setFirstResult(offset);
        query.setMaxResults(pageable.getPageSize());

        List<Tag> results = query.getResultList().stream().map(TagBox::getEntity).toList();

        long count;

        if (results.size() < pageable.getPageSize()) {
            count = results.size();
        } else {
            cq.orderBy(Collections.emptyList());
            cq.multiselect(cb.countDistinct(root.get(Tag_.id)));
            count = entityManager.createQuery(cq).getSingleResult().getCount();
        }

        return new Page<>(results, pageable, count);
    }

    @Override
    public Optional<Tag> findByName(String tagName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
        Root<Tag> root = cq.from(Tag.class);

        cq.select(root).where(cb.like(cb.lower(root.get(Tag_.name).as(String.class)), tagName.toLowerCase()));
        TypedQuery<Tag> query = entityManager.createQuery(cq);

        List<Tag> result = query.getResultList();

        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return result.stream().findFirst();
        }
    }
}

@Getter
class TagBox {
    long count;
    Tag entity;

    TagBox(long c) {
        count = c;
    }

    TagBox(Tag e) {
        entity = e;
    }
}
