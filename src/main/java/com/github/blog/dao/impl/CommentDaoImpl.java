package com.github.blog.dao.impl;

import com.github.blog.dao.CommentDao;
import com.github.blog.model.Comment;
import com.github.blog.model.Comment_;
import com.github.blog.model.User_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Repository
public class CommentDaoImpl extends AbstractJpaDao<Comment, Long> implements CommentDao {

    public List<Comment> findAllByLogin(String login) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> cq = cb.createQuery(Comment.class);

        Root<Comment> commentRoot = cq.from(Comment.class);

        commentRoot.fetch(Comment_.user, JoinType.LEFT);
        cq.select(commentRoot);
        Predicate loginPredicate = cb.equal(commentRoot.get(Comment_.user).get(User_.login), login);
        cq.where(loginPredicate);

        TypedQuery<Comment> query = entityManager.createQuery(cq);

        return query.getResultList();
    }
}

