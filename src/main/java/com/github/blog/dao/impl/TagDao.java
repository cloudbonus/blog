package com.github.blog.dao.impl;

import com.github.blog.dao.Dao;
import com.github.blog.model.Tag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class TagDao implements Dao<Tag> {
    private final List<Tag> tags = new ArrayList<>();

    @Override
    public Optional<Tag> getById(int id) {
        return tags.stream().filter(t -> t.getTagId() == id).findAny();
    }

    @Override
    public List<Tag> getAll() {
        return tags;
    }

    @Override
    public int save(Tag tag) {
        tags.add(tag);
        int index = tags.size();
        tag.setTagId(index);
        return index;
    }

    @Override
    public boolean update(Tag tag) {
        if (tags.stream().map(t -> t.getTagId() == tag.getTagId()).findAny().orElse(false)) {
            tags.set(tag.getTagId() - 1, tag);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return tags.removeIf(t -> t.getTagId() == id);
    }
}
