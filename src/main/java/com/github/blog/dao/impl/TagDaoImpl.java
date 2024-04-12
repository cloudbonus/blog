package com.github.blog.dao.impl;

import com.github.blog.dao.TagDao;
import com.github.blog.model.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Raman Haurylau
 */
@Repository
public class TagDaoImpl implements TagDao {
    private static final List<Tag> TAGS = new CopyOnWriteArrayList<>();

    @Override
    public Optional<Tag> findById(Integer id) {
        return TAGS.stream().filter(t -> t.getId() == id).findAny();
    }

    @Override
    public List<Tag> findAll() {
        return TAGS;
    }

    @Override
    public Tag create(Tag tag) {
        TAGS.add(tag);
        int index = TAGS.size();
        tag.setId(index);
        return tag;
    }

    @Override
    public Tag update(Tag tag) {
        for (int i = 0; i < TAGS.size(); i++) {
            if (TAGS.get(i).getId() == tag.getId()) {
                TAGS.set(i, tag);
                return tag;
            }
        }
        return null;
    }

    @Override
    public Tag remove(Integer id) {
        Tag tagToRemove = null;

        for (Tag t : TAGS) {
            if (t.getId() == id) {
                tagToRemove = t;
                break;
            }
        }

        if (tagToRemove != null) {
            TAGS.remove(tagToRemove);
        }

        return tagToRemove;
    }
}
