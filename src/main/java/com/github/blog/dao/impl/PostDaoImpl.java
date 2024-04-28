package com.github.blog.dao.impl;

import com.github.blog.dao.PostDao;
import com.github.blog.model.Post;
import com.github.blog.model.Post_;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Repository
public class PostDaoImpl extends AbstractJpaDao<Post, Long> implements PostDao {

    public List<Post> findAllByLogin(String login) {
        String jpql = "select p from Post p join fetch p.user u where u.login = :login";
        TypedQuery<Post> query = entityManager.createQuery(jpql, Post.class);
        query.setParameter("login", login);
        return query.getResultList();
    }

    public List<Post> findAllByTag(String tagName) {
        EntityGraph<Post> graph = entityManager.createEntityGraph(Post.class);
        graph.addSubgraph(Post_.user);

        String jpql = "SELECT p FROM Post p JOIN p.tags t WHERE t.tagName = :tagName";

        TypedQuery<Post> query = entityManager.createQuery(jpql, Post.class);
        query.setParameter("tagName", tagName);
        query.setHint("jakarta.persistence.loadgraph", graph);

        return query.getResultList();
    }
}
