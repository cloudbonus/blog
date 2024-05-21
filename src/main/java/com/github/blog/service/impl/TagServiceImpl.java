package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.TagDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.TagRequest;
import com.github.blog.controller.dto.request.filter.TagDtoFilter;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Tag;
import com.github.blog.repository.TagDao;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.TagFilter;
import com.github.blog.service.TagService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.TagException;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.TagMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Raman Haurylau
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final TagMapper tagMapper;
    private final PageableMapper pageableMapper;

    @Override
    public TagDto create(TagRequest request) {
        Tag tag = tagMapper.toEntity(request);
        return tagMapper.toDto(tagDao.create(tag));
    }

    @Override
    public TagDto findById(Long id) {
        Tag tag = tagDao
                .findById(id)
                .orElseThrow(() -> new TagException(ExceptionEnum.TAG_NOT_FOUND));

        return tagMapper.toDto(tag);
    }

    @Override
    public Page<TagDto> findAll(TagDtoFilter filterRequest, PageableRequest pageableRequest) {
        TagFilter dtoFilter = tagMapper.toDto(filterRequest);
        Pageable pageable = pageableMapper.toDto(pageableRequest);

        Page<Tag> tags = tagDao.findAll(dtoFilter, pageable);

        if (tags.isEmpty()) {
            throw new TagException(ExceptionEnum.TAGS_NOT_FOUND);
        }

        return tags.map(tagMapper::toDto);
    }

    @Override
    public TagDto update(Long id, TagRequest request) {
        Tag tag = tagDao
                .findById(id)
                .orElseThrow(() -> new TagException(ExceptionEnum.TAG_NOT_FOUND));

        tag = tagMapper.partialUpdate(request, tag);

        return tagMapper.toDto(tag);
    }

    @Override
    public TagDto delete(Long id) {
        Tag tag = tagDao
                .findById(id)
                .orElseThrow(() -> new TagException(ExceptionEnum.TAG_NOT_FOUND));

        tagDao.delete(tag);

        return tagMapper.toDto(tag);
    }
}
