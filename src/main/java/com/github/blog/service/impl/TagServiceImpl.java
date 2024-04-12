package com.github.blog.service.impl;

import com.github.blog.dao.TagDao;
import com.github.blog.dto.TagDto;
import com.github.blog.mapper.Mapper;
import com.github.blog.model.Tag;
import com.github.blog.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final Mapper mapper;

    @Override
    public TagDto create(TagDto tagDto) {
        Tag tag = mapper.map(tagDto, Tag.class);
        return mapper.map(tagDao.create(tag), TagDto.class);
    }

    @Override
    public TagDto findById(int id) {
        Optional<Tag> result = tagDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }
        return mapper.map(result.get(), TagDto.class);
    }

    @Override
    public List<TagDto> findAll() {
        List<Tag> tags = tagDao.findAll();
        if (tags.isEmpty()) {
            throw new RuntimeException("Cannot find any tags");
        }
        return tags.stream().map(t -> mapper.map(t, TagDto.class)).toList();
    }

    @Override
    public TagDto update(int id, TagDto tagDto) {
        Optional<Tag> result = tagDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }

        Tag updatedTag = mapper.map(tagDto, Tag.class);
        Tag tag = result.get();

        updatedTag.setId(tag.getId());

        updatedTag = tagDao.update(updatedTag);

        return mapper.map(updatedTag, TagDto.class);
    }

    @Override
    public int remove(int id) {
        Tag tag = tagDao.remove(id);
        if (tag == null) {
            return -1;
        } else return tag.getId();
    }
}
