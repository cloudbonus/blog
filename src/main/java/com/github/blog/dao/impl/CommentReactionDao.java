package com.github.blog.dao.impl;

import com.github.blog.dao.Dao;
import com.github.blog.model.CommentReaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * @author Raman Haurylau
 */
@Component
public class CommentReactionDao implements Dao<CommentReaction> {
    private final List<CommentReaction> reactions = new ArrayList<>();

//    {
//        reactions = new ArrayList<>();
//        reactions.add(new CommentReaction(++REACTION_COUNT, "Like", 1, 1));
//        reactions.add(new CommentReaction(++REACTION_COUNT, "Dislike", 2, 1));
//    }

    @Override
    public Optional<CommentReaction> getById(int id) {
        return reactions.stream().filter(r -> r.getCommentId() == id).findAny();
    }

    @Override
    public List<CommentReaction> getAll() {
        return reactions;
    }

    @Override
    public int save(CommentReaction reaction) {
        reactions.add(reaction);
        int index = reactions.size();
        reaction.setCommentId(index);
        return index;
    }

    @Override
    public boolean update(CommentReaction reaction) {
        if (reactions.stream().map(r -> r.getCommentId() == reaction.getCommentId()).findAny().orElse(false)) {
            reactions.set(reaction.getCommentId() - 1, reaction);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return reactions.removeIf(r -> r.getCommentId() == id);
    }
}


