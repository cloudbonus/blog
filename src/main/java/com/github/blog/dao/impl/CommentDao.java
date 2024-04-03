package com.github.blog.dao.impl;

import com.github.blog.dao.Dao;
import com.github.blog.model.Comment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class CommentDao implements Dao<Comment> {
    private final List<Comment> comments = new ArrayList<>();

    @Override
    public Optional<Comment> getById(int id) {
        return comments.stream().filter(c -> c.getCommentId() == id).findAny();
    }

    @Override
    public List<Comment> getAll() {
        return comments;
    }

    @Override
    public int save(Comment comment) {
        comments.add(comment);
        int index = comments.size();
        comment.setCommentId(index);
        return index;
    }

    @Override
    public boolean update(Comment comment) {

        if (comments.stream().map(c -> c.getCommentId() == comment.getCommentId()).findAny().orElse(false)) {
            comments.set(comment.getCommentId() - 1, comment);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return comments.removeIf(c -> c.getCommentId() == id);
    }
}

