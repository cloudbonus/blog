package com.github.blog.dao.impl;

import com.github.blog.dao.PostReactionDao;
import com.github.blog.model.PostReaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Raman Haurylau
 */
@Repository
public class PostReactionDaoImpl implements PostReactionDao {
    private static final List<PostReaction> POST_REACTIONS = new CopyOnWriteArrayList<>();

    @Override
    public Optional<PostReaction> findById(Integer id) {
        return POST_REACTIONS.stream().filter(r -> r.getId() == id).findAny();
    }

    @Override
    public List<PostReaction> findAll() {
        return POST_REACTIONS;
    }

    @Override
    public PostReaction create(PostReaction reaction) {
        POST_REACTIONS.add(reaction);
        int index = POST_REACTIONS.size();
        reaction.setId(index);
        return reaction;
    }

    @Override
    public PostReaction update(PostReaction reaction) {
        for (int i = 0; i < POST_REACTIONS.size(); i++) {
            if (POST_REACTIONS.get(i).getId() == reaction.getId()) {
                POST_REACTIONS.set(i, reaction);
                return reaction;
            }
        }
        return null;
    }

    @Override
    public PostReaction remove(Integer id) {
        PostReaction postReactionToRemove = null;

        for (PostReaction p : POST_REACTIONS) {
            if (p.getId() == id) {
                postReactionToRemove = p;
                break;
            }
        }

        if (postReactionToRemove != null) {
            POST_REACTIONS.remove(postReactionToRemove);
        }

        return postReactionToRemove;
    }
}

