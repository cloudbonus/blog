package com.github.blog.service.impl;

import com.github.blog.controller.dto.common.TagDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.TagRequest;
import com.github.blog.controller.dto.request.filter.TagFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Tag;
import com.github.blog.repository.TagDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.TagFilter;
import com.github.blog.service.TagService;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    private final TagMapper tagMapper;
    private final PageableMapper pageableMapper;

    @Override
    public TagDto create(TagRequest request) {
        log.debug("Creating a new tag with request: {}", request);
        Tag tag = tagMapper.toEntity(request);

        tag = tagDao.create(tag);
        log.debug("Tag created successfully with ID: {}", tag.getId());
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto findById(Long id) {
        log.debug("Finding tag by ID: {}", id);
        Tag tag = tagDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.TAG_NOT_FOUND));

        log.debug("Tag found with ID: {}", id);
        return tagMapper.toDto(tag);
    }

    @Override
    public PageResponse<TagDto> findAll(TagFilterRequest filterRequest, PageableRequest pageableRequest) {
        log.debug("Finding all tags with filter: {} and pageable: {}", filterRequest, pageableRequest);
        TagFilter filter = tagMapper.toEntity(filterRequest);
        Pageable pageable = pageableMapper.toEntity(pageableRequest);

        Page<Tag> tags = tagDao.findAll(filter, pageable);

        if (tags.isEmpty()) {
            throw new CustomException(ExceptionEnum.TAGS_NOT_FOUND);
        }

        log.debug("Found {} tags", tags.getTotalNumberOfEntities());
        return tagMapper.toDto(tags);
    }

    @Override
    public TagDto update(Long id, TagRequest request) {
        log.debug("Updating tag with ID: {} and request: {}", id, request);
        Tag tag = tagDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.TAG_NOT_FOUND));

        tag = tagMapper.partialUpdate(request, tag);
        log.debug("Tag updated successfully with ID: {}", id);
        return tagMapper.toDto(tag);
    }

    @Override
    public TagDto delete(Long id) {
        log.debug("Deleting tag with ID: {}", id);
        Tag tag = tagDao
                .findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.TAG_NOT_FOUND));

        tagDao.delete(tag);
        log.debug("Tag deleted successfully with ID: {}", id);
        return tagMapper.toDto(tag);
    }
}
