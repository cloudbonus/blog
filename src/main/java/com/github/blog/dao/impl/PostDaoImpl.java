package com.github.blog.dao.impl;

import com.github.blog.dao.PostDao;
import com.github.blog.dto.filter.PostDtoFilter;
import com.github.blog.model.Post;
import com.github.blog.model.Post_;
import com.github.blog.model.Tag;
import com.github.blog.model.Tag_;
import com.github.blog.model.User;
import com.github.blog.model.User_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Repository
public class PostDaoImpl extends AbstractJpaDao<Post, Long> implements PostDao {

    @Override
    public List<Post> findAll(PostDtoFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> post = cq.from(Post.class);
        Join<Post, User> user = post.join(Post_.user, JoinType.LEFT);
        Join<Post, Tag> tag = post.join(Post_.tags, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(filter.getLogin())) {
            predicates.add(cb.equal(cb.lower(user.get(User_.login).as(String.class)), filter.getLogin().toLowerCase()));
        }

        if (!ObjectUtils.isEmpty(filter.getTag())) {
            predicates.add(cb.equal(cb.lower(tag.get(Tag_.tagName).as(String.class)), filter.getTag().toLowerCase()));
        }

        cq.orderBy(cb.asc(post.get(Post_.id)));

        cq.where(cb.and(predicates.toArray(Predicate[]::new)));
        TypedQuery<Post> query = entityManager.createQuery(cq.select(post).distinct(true));

        return query.getResultList();
    }
}
