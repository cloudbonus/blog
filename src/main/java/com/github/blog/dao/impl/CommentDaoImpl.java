package com.github.blog.dao.impl;

import com.github.blog.dao.CommentDao;
import com.github.blog.dto.filter.CommentFilter;
import com.github.blog.model.Comment;
import com.github.blog.model.Comment_;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Repository
@Transactional
public class CommentDaoImpl extends AbstractJpaDao<Comment, Long> implements CommentDao {

    @Override
    public List<Comment> findAll(CommentFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Comment> cq = cb.createQuery(Comment.class);
        Root<Comment> comment = cq.from(Comment.class);
        Join<Comment, User> user = comment.join(Comment_.user, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();


        if (!ObjectUtils.isEmpty(filter.getLogin())) {
            predicates.add(cb.equal(cb.lower(user.get(User_.login).as(String.class)), filter.getLogin().toLowerCase()));
        }

        cq.orderBy(cb.asc(comment.get(Comment_.id)));

        cq.where(cb.and(predicates.toArray(Predicate[]::new)));
        TypedQuery<Comment> query = entityManager.createQuery(cq.select(comment).distinct(true));

        return query.getResultList();
    }
}

