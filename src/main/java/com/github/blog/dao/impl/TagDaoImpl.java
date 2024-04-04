package com.github.blog.dao.impl;

import com.github.blog.dao.TagDao;
import com.github.blog.model.Tag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class TagDaoImpl implements TagDao {
    private final List<Tag> tags = new ArrayList<>();

    @Override
    public Optional<Tag> getById(int id) {
        return tags.stream().filter(t -> t.getId() == id).findAny();
    }

    @Override
    public List<Tag> getAll() {
        return tags;
    }

    @Override
    public int save(Tag tag) {
        tags.add(tag);
        int index = tags.size();
        tag.setId(index);
        return index;
    }

    @Override
    public Optional<Tag> update(Tag tag) {
        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i).getId() == tag.getId()) {
                tags.set(i, tag);
                return Optional.of(tag);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        return tags.removeIf(t -> t.getId() == id);
    }
}
