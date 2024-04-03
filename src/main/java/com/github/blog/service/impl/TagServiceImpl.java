package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.TagDao;
import com.github.blog.dto.TagDto;
import com.github.blog.model.Tag;
import com.github.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, ObjectMapper objectMapper) {
        this.tagDao = tagDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public int create(TagDto tagDto) {
        Tag tag = convertToObject(tagDto);
        return tagDao.save(tag);
    }

    @Override
    public TagDto readById(int id) {
        Optional<Tag> result = tagDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<TagDto> readAll() {
        List<Tag> tags = tagDao.getAll();
        if (tags.isEmpty()) {
            throw new RuntimeException("Cannot find any tags");
        }
        return tags.stream().map(this::convertToDto).toList();
    }

    @Override
    public TagDto update(int id, TagDto tagDto) {
        Optional<Tag> result = tagDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }

        Tag updatedTag = convertToObject(tagDto);
        Tag tag = result.get();

        updatedTag.setId(tag.getId());

        result = tagDao.update(updatedTag);

        if (result.isEmpty()) {
            throw new RuntimeException("Couldn't update tag");
        }

        return convertToDto(result.get());
    }

    @Override
    public boolean delete(int id) {
        return tagDao.deleteById(id);
    }

    private Tag convertToObject(TagDto tagDto) {
        return objectMapper.convertValue(tagDto, Tag.class);
    }

    private TagDto convertToDto(Tag tag) {
        return objectMapper.convertValue(tag, TagDto.class);
    }
}
