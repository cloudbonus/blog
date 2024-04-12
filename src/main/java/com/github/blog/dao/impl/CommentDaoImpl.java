package com.github.blog.dao.impl;

import com.github.blog.dao.CommentDao;
import com.github.blog.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Raman Haurylau
 */
@Repository
public class CommentDaoImpl implements CommentDao {
    private static final List<Comment> COMMENTS = new CopyOnWriteArrayList<>();

    @Override
    public Optional<Comment> findById(Integer id) {
        return COMMENTS.stream().filter(c -> c.getId() == id).findAny();
    }

    @Override
    public List<Comment> findAll() {
        return COMMENTS;
    }

    @Override
    public Comment create(Comment comment) {
        COMMENTS.add(comment);
        int index = COMMENTS.size();
        comment.setId(index);
        return comment;
    }

    @Override
    public Comment update(Comment comment) {
        for (int i = 0; i < COMMENTS.size(); i++) {
            if (COMMENTS.get(i).getId() == comment.getId()) {
                COMMENTS.set(i, comment);
                return comment;
            }
        }
        return null;
    }

    @Override
    public Comment remove(Integer id) {
        Comment commentToRemove = null;

        for (Comment c : COMMENTS) {
            if (c.getId() == id) {
                commentToRemove = c;
                break;
            }
        }

        if (commentToRemove != null) {
            COMMENTS.remove(commentToRemove);
        }

        return commentToRemove;
    }
}

