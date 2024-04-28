package com.github.blog.service.impl;

import com.github.blog.dao.TagDao;
import com.github.blog.dto.TagDto;
import com.github.blog.model.Tag;
import com.github.blog.service.TagService;
import com.github.blog.service.exception.TagErrorResult;
import com.github.blog.service.exception.impl.TagException;
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
        Tag tag = tagDao
                .findById(id)
                .orElseThrow(() -> new TagException(TagErrorResult.TAG_NOT_FOUND));

        return tagMapper.toDto(tag);
    }

    @Override
    public List<TagDto> findAll() {
        List<Tag> tags = tagDao.findAll();

        if (tags.isEmpty()) {
            throw new TagException(TagErrorResult.TAGS_NOT_FOUND);
        }

        return tags.stream().map(tagMapper::toDto).toList();
    }

    @Override
    public TagDto update(Long id, TagDto tagDto) {
        Tag tag = tagDao
                .findById(id)
                .orElseThrow(() -> new TagException(TagErrorResult.TAG_NOT_FOUND));

        tag = tagMapper.partialUpdate(tagDto, tag);
        tag = tagDao.update(tag);

        return tagMapper.toDto(tag);
    }

    @Override
    public TagDto delete(Long id) {
        Tag tag = tagDao
                .findById(id)
                .orElseThrow(() -> new TagException(TagErrorResult.TAG_NOT_FOUND));
        tagDao.delete(tag);
        return tagMapper.toDto(tag);
    }
}
