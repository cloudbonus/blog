package com.github.blog.dao.impl;

import com.github.blog.dao.PostDao;
import com.github.blog.model.Post;
import com.github.blog.model.PostTag;
import com.github.blog.model.PostTagId;
import com.github.blog.model.Post_;
import com.github.blog.model.Tag;
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

        String jpql = "SELECT p FROM Post p JOIN PostTag pt ON p.id = pt.post.id JOIN Tag t ON pt.tag.id = t.id WHERE t.tagName = :tagName";
        TypedQuery<Post> query = entityManager.createQuery(jpql, Post.class);

        query.setParameter("tagName", tagName);
        query.setHint("javax.persistence.loadgraph", graph);

        return query.getResultList();
    }

    public Post updateTags(Post post, List<Tag> tags) {
        entityManager.createQuery("delete from PostTag pt where pt.post.id = :postId")
                .setParameter("postId", post.getId())
                .executeUpdate();

        for (Tag tag : tags) {
            PostTag postTag = new PostTag();
            postTag.setPost(post);
            postTag.setTag(tag);
            PostTagId postTagId = new PostTagId();
            postTagId.setPostId(post.getId());
            postTagId.setTagId(tag.getId());
            postTag.setId(postTagId);
            entityManager.persist(postTag);
        }
        entityManager.refresh(post);
        return post;
    }
}
