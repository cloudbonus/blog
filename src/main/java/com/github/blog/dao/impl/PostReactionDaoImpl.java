package com.github.blog.dao.impl;

import com.github.blog.dao.PostReactionDao;
import com.github.blog.model.PostReaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class PostReactionDaoImpl implements PostReactionDao {
    private final List<PostReaction> reactions = new ArrayList<>();

    @Override
    public Optional<PostReaction> getById(int id) {
        return reactions.stream().filter(r -> r.getId() == id).findAny();
    }

    @Override
    public List<PostReaction> getAll() {
        return reactions;
    }

    @Override
    public int save(PostReaction reaction) {
        reactions.add(reaction);
        int index = reactions.size();
        reaction.setId(index);
        return index;
    }

    @Override
    public Optional<PostReaction> update(PostReaction reaction) {
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

