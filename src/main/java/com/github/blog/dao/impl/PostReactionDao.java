package com.github.blog.dao.impl;

import com.github.blog.dao.Dao;
import com.github.blog.model.PostReaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class PostReactionDao implements Dao<PostReaction> {
    private final List<PostReaction> reactions = new ArrayList<>();

    @Override
    public Optional<PostReaction> getById(int id) {
        return reactions.stream().filter(r -> r.getPostId() == id).findAny();
    }

    @Override
    public List<PostReaction> getAll() {
        return reactions;
    }

    @Override
    public int save(PostReaction reaction) {
        reactions.add(reaction);
        int index = reactions.size();
        reaction.setPostId(index);
        return index;
    }

    @Override
    public boolean update(PostReaction reaction) {
        if (reactions.stream().map(o -> o.getPostId() == reaction.getPostId()).findAny().orElse(false)) {
            reactions.set(reaction.getPostId() - 1, reaction);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return reactions.removeIf(r -> r.getPostId() == id);
    }
}

