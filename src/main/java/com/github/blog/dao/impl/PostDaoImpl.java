package com.github.blog.dao.impl;

import com.github.blog.dao.PostDao;
import com.github.blog.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Raman Haurylau
 */
@Repository
public class PostDaoImpl implements PostDao {
    private static final List<Post> POSTS = new CopyOnWriteArrayList<>();

    @Override
    public Optional<Post> findById(Integer id) {
        return POSTS.stream().filter(p -> p.getId() == id).findAny();
    }

    @Override
    public List<Post> findAll() {
        return POSTS;
    }

    @Override
    public Post create(Post post) {
        POSTS.add(post);
        int index = POSTS.size();
        post.setId(index);
        return post;
    }

    @Override
    public Post update(Post post) {
        for (int i = 0; i < POSTS.size(); i++) {
            if (POSTS.get(i).getId() == post.getId()) {
                POSTS.set(i, post);
                return post;
            }
        }
        return null;
    }

    @Override
    public Post remove(Integer id) {
        Post postToRemove = null;

        for (Post p : POSTS) {
            if (p.getId() == id) {
                postToRemove = p;
                break;
            }
        }

        if (postToRemove != null) {
            POSTS.remove(postToRemove);
        }

        return postToRemove;
    }
}
