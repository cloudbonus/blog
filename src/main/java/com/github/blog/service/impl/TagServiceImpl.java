package com.github.blog.service.impl;

import com.github.blog.dao.TagDao;
import com.github.blog.dto.TagDto;
import com.github.blog.model.Tag;
import com.github.blog.service.TagService;
import com.github.blog.service.mapper.TagMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final TagMapper tagMapper;

    @Override
    public TagDto create(TagDto tagDto) {
        Tag tag = tagMapper.toEntity(tagDto);
        return tagMapper.toDto(tagDao.create(tag));
    }

    @Override
    public TagDto findById(Long id) {
        Tag tag = tagDao.findById(id);
        if (tag == null) {
            throw new RuntimeException("Tag not found");
        }
        return tagMapper.toDto(tag);
    }

    @Override
    public List<TagDto> findAll() {
        List<Tag> tags = tagDao.findAll();
        if (tags.isEmpty()) {
            throw new RuntimeException("Cannot find any tags");
        }
        return tags.stream().map(tagMapper::toDto).toList();
    }

    @Override
    public TagDto update(Long id, TagDto tagDto) {
        Tag tag = tagDao.findById(id);

        if (tag == null) {
            throw new RuntimeException("Tag not found");
        }

        Tag updatedTag = tagMapper.toEntity(tagDto);

        updatedTag.setId(tag.getId());
        updatedTag = tagDao.update(updatedTag);

        return tagMapper.toDto(updatedTag);
    }

    @Override
    public void delete(Long id) {
        tagDao.deleteById(id);
    }
}
