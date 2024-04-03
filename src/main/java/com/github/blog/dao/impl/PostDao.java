package com.github.blog.dao.impl;

import com.github.blog.dao.Dao;
import com.github.blog.model.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class PostDao implements Dao<Post> {
    private final List<Post> posts = new ArrayList<>();

//    {
//        posts = new ArrayList<>();
//        posts.add(new Post(++POST_COUNT, "Mr", "Donec diam neque, vestibulum eget, vulputate ut, ultrices vel, augue.", LocalDateTime.now(), 1));
//        posts.add(new Post(++POST_COUNT, "Dr", "Nullam porttitor lacus at turpis. Donec posuere metus vitae ipsum. Aliquam non mauris.", LocalDateTime.now(), 1));
//        posts.add(new Post(++POST_COUNT, "Honorable", "Quisque arcu libero, rutrum ac, lobortis vel, dapibus at, diam.", LocalDateTime.now(), 2));
//    }

    @Override
    public Optional<Post> getById(int id) {
        return posts.stream().filter(p -> p.getPostId() == id).findAny();
    }

    @Override
    public List<Post> getAll() {
        return posts;
    }

    @Override
    public int save(Post post) {
        posts.add(post);
        int index = posts.size();
        post.setPostId(index);
        return index;
    }

    @Override
    public boolean update(Post post) {
        if (posts.stream().map(u -> u.getPostId() == post.getPostId()).findAny().orElse(false)) {
            posts.set(post.getPostId() - 1, post);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return posts.removeIf(p -> p.getPostId() == id);
    }
}
