package com.github.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.dao.Dao;
import com.github.blog.dto.TagDto;
import com.github.blog.model.Tag;
import com.github.blog.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Component
public class TagService implements Service<Serializable> {

    private final Dao<Tag> tagDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public TagService(Dao<Tag> tagDao, ObjectMapper objectMapper) {
        this.tagDao = tagDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public int create(Serializable tagDto) {
        Tag tag = convertToObject(tagDto);
        return tagDao.save(tag);
    }

    @Override
    public Serializable readById(int id) {
        Optional<Tag> result = tagDao.getById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }
        return convertToDto(result.get());
    }

    @Override
    public List<Serializable> readAll() {
        List<Tag> tags = tagDao.getAll();
        if (tags.isEmpty()) {
            throw new RuntimeException("Cannot find any tags");
        }
        return tags.stream().map(this::convertToDto).toList();
    }

    @Override
    public boolean update(int id, Serializable tagDto) {
        Optional<Tag> result = tagDao.getById(id);

        if (result.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }

        Tag tag = convertToObject(tagDto);
        tag.setTagId(id);
        return tagDao.update(tag);
    }

    @Override
    public boolean delete(int id) {
        return tagDao.deleteById(id);
    }

    private Tag convertToObject(Serializable tagDto) {
        return objectMapper.convertValue(tagDto, Tag.class);
    }

    private Serializable convertToDto(Tag tag) {
        return objectMapper.convertValue(tag, TagDto.class);
    }
}
