package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.TagDao;
import com.github.blog.dto.TagDto;
import com.github.blog.model.Tag;
import com.github.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, ObjectMapper objectMapper) {
        this.tagDao = tagDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public TagDto create(TagDto tagDto) {
        Tag tag = convertToObject(tagDto);
        return convertToDto(tagDao.create(tag));
    }

    @Override
    public TagDto findById(int id) {
        Optional<Tag> result = tagDao.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<TagDto> findAll() {
        List<Tag> tags = tagDao.findAll();
        if (tags.isEmpty()) {
            throw new RuntimeException("Cannot find any tags");
        }
        return tags.stream().map(this::convertToDto).toList();
    }

    @Override
    public TagDto update(int id, TagDto tagDto) {
        Optional<Tag> result = tagDao.findById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }

        Tag updatedTag = convertToObject(tagDto);
        Tag tag = result.get();

        updatedTag.setId(tag.getId());

        updatedTag = tagDao.update(updatedTag);

        return convertToDto(updatedTag);
    }

    @Override
    public int remove(int id) {
        Tag tag = tagDao.remove(id);
        if (tag == null) {
            return -1;
        } else return tag.getId();
    }

    private Tag convertToObject(TagDto tagDto) {
        return objectMapper.convertValue(tagDto, Tag.class);
    }

    private TagDto convertToDto(Tag tag) {
        return objectMapper.convertValue(tag, TagDto.class);
    }
}
