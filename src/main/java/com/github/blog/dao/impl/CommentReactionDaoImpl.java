package com.github.blog.dao.impl;

import com.github.blog.dao.CommentReactionDao;
import com.github.blog.model.CommentReaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class CommentReactionDaoImpl implements CommentReactionDao {
    private final List<CommentReaction> reactions = new ArrayList<>();

    @Override
    public Optional<CommentReaction> getById(int id) {
        return reactions.stream().filter(r -> r.getId() == id).findAny();
    }

    @Override
    public List<CommentReaction> getAll() {
        return reactions;
    }

    @Override
    public int save(CommentReaction reaction) {
        reactions.add(reaction);
        int index = reactions.size();
        reaction.setId(index);
        return index;
    }

    @Override
    public Optional<CommentReaction> update(CommentReaction reaction) {
        for (int i = 0; i < reactions.size(); i++) {
            if (reactions.get(i).getId() == reaction.getId()) {
                reactions.set(i, reaction);
                return Optional.of(reaction);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        return reactions.removeIf(r -> r.getId() == id);
    }
}


