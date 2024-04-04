package com.github.blog.dao.impl;

import com.github.blog.dao.CommentDao;
import com.github.blog.model.Comment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class CommentDaoImpl implements CommentDao {
    private final List<Comment> comments = new ArrayList<>();

    @Override
    public Optional<Comment> getById(int id) {
        return comments.stream().filter(c -> c.getId() == id).findAny();
    }

    @Override
    public List<Comment> getAll() {
        return comments;
    }

    @Override
    public int save(Comment comment) {
        comments.add(comment);
        int index = comments.size();
        comment.setId(index);
        return index;
    }

    @Override
    public Optional<Comment> update(Comment comment) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getId() == comment.getId()) {
                comments.set(i, comment);
                return Optional.of(comment);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        return comments.removeIf(c -> c.getId() == id);
    }
}

