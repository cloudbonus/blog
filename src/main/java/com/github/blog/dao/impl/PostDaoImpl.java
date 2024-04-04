package com.github.blog.dao.impl;

import com.github.blog.dao.PostDao;
import com.github.blog.model.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class PostDaoImpl implements PostDao {
    private final List<Post> posts = new ArrayList<>();

    @Override
    public Optional<Post> getById(int id) {
        return posts.stream().filter(p -> p.getId() == id).findAny();
    }

    @Override
    public List<Post> getAll() {
        return posts;
    }

    @Override
    public int save(Post post) {
        posts.add(post);
        int index = posts.size();
        post.setId(index);
        return index;
    }

    @Override
    public Optional<Post> update(Post post) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == post.getId()) {
                posts.set(i, post);
                return Optional.of(post);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        return posts.removeIf(p -> p.getId() == id);
    }
}
