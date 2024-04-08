package com.github.blog.dao.impl;

import com.github.blog.dao.CommentReactionDao;
import com.github.blog.model.CommentReaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Raman Haurylau
 */
@Repository
public class CommentReactionDaoImpl implements CommentReactionDao {
    private static final List<CommentReaction> COMMENT_REACTIONS = new CopyOnWriteArrayList<>();

    @Override
    public Optional<CommentReaction> findById(Integer id) {
        return COMMENT_REACTIONS.stream().filter(r -> r.getId() == id).findAny();
    }

    @Override
    public List<CommentReaction> findAll() {
        return COMMENT_REACTIONS;
    }

    @Override
    public CommentReaction create(CommentReaction reaction) {
        COMMENT_REACTIONS.add(reaction);
        int index = COMMENT_REACTIONS.size();
        reaction.setId(index);
        return reaction;
    }

    @Override
    public CommentReaction update(CommentReaction reaction) {
        for (int i = 0; i < COMMENT_REACTIONS.size(); i++) {
            if (COMMENT_REACTIONS.get(i).getId() == reaction.getId()) {
                COMMENT_REACTIONS.set(i, reaction);
                return reaction;
            }
        }
        return null;
    }

    @Override
    public CommentReaction remove(Integer id) {
        CommentReaction commentReactionToRemove = null;

        for (CommentReaction r : COMMENT_REACTIONS) {
            if (r.getId() == id) {
                commentReactionToRemove = r;
                break;
            }
        }

        if (commentReactionToRemove != null) {
            COMMENT_REACTIONS.remove(commentReactionToRemove);
        }

        return commentReactionToRemove;
    }
}


