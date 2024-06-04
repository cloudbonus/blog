package com.github.blog.service;

import com.github.blog.controller.dto.common.TagDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.TagRequest;
import com.github.blog.controller.dto.request.filter.TagFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.controller.dto.response.PageableResponse;
import com.github.blog.model.Tag;
import com.github.blog.repository.TagDao;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.TagFilter;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.impl.TagServiceImpl;
import com.github.blog.service.mapper.PageableMapper;
import com.github.blog.service.mapper.TagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Raman Haurylau
 */
@ExtendWith(MockitoExtension.class)
public class TagServiceImplTests {

    @Mock
    private TagDao tagDao;

    @Mock
    private TagMapper tagMapper;

    @Mock
    private PageableMapper pageableMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private TagRequest request;
    private TagDto returnedTagDto;
    private Tag tag;

    private final Long id = 1L;
    private final String tagName = "news";

    private final Pageable pageable = new Pageable();
    private final PageableResponse pageableResponse = new PageableResponse();

    @BeforeEach
    void setUp() {
        request = new TagRequest();
        request.setName(tagName);

        tag = new Tag();
        tag.setId(id);
        tag.setName(tagName);

        returnedTagDto = new TagDto();
        returnedTagDto.setId(id);
        returnedTagDto.setName(tagName);
    }

    @Test
    @DisplayName("tag service: create")
    void create_returnsTagDto_whenDataIsValid() {
        when(tagMapper.toEntity(request)).thenReturn(tag);
        when(tagDao.create(tag)).thenReturn(tag);
        when(tagMapper.toDto(tag)).thenReturn(returnedTagDto);

        TagDto createdTagDto = tagService.create(request);

        assertNotNull(createdTagDto);
        assertEquals(id, createdTagDto.getId());
        assertEquals(tagName, createdTagDto.getName());
    }

    @Test
    @DisplayName("tag service: find by id")
    void findById_returnsTagDto_whenIdIsValid() {
        when(tagDao.findById(id)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(returnedTagDto);

        TagDto foundTagDto = tagService.findById(id);

        assertNotNull(foundTagDto);
        assertEquals(id, foundTagDto.getId());
        assertEquals(tagName, foundTagDto.getName());
    }

    @Test
    @DisplayName("tag service: find by id - not found exception")
    void findById_throwsException_whenIdIsInvalid() {
        when(tagDao.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> tagService.findById(id));

        assertEquals(ExceptionEnum.TAG_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("tag service: find all")
    void findAll_returnsPageResponse_whenDataIsValid() {
        TagFilter filter = new TagFilter();

        Page<Tag> page = new Page<>(Collections.singletonList(tag), pageable, 1L);
        PageResponse<TagDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedTagDto), pageableResponse, 1L);

        when(tagMapper.toEntity(any(TagFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(tagDao.findAll(filter, pageable)).thenReturn(page);
        when(tagMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<TagDto> foundTags = tagService.findAll(new TagFilterRequest(), new PageableRequest());

        assertNotNull(foundTags);
        assertEquals(1, foundTags.getContent().size());
        assertEquals(id, foundTags.getContent().get(0).getId());
        assertEquals(tagName, foundTags.getContent().get(0).getName());
    }

    @Test
    @DisplayName("tag service: find all - not found exception")
    void findAll_throwsException_whenNoTagsFound() {
        TagFilter filter = new TagFilter();

        Page<Tag> page = new Page<>(Collections.emptyList(), pageable, 1L);

        when(tagMapper.toEntity(any(TagFilterRequest.class))).thenReturn(filter);
        when(pageableMapper.toEntity(any(PageableRequest.class))).thenReturn(pageable);
        when(tagDao.findAll(filter, pageable)).thenReturn(page);

        CustomException exception = assertThrows(CustomException.class, () -> tagService.findAll(new TagFilterRequest(), new PageableRequest()));

        assertEquals(ExceptionEnum.TAGS_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("tag service: update")
    void update_returnsUpdatedTagDto_whenDataIsValid() {
        when(tagDao.findById(id)).thenReturn(Optional.of(tag));
        when(tagMapper.partialUpdate(request, tag)).thenReturn(tag);
        when(tagMapper.toDto(tag)).thenReturn(returnedTagDto);

        TagDto updatedTagDto = tagService.update(id, request);

        assertNotNull(updatedTagDto);
        assertEquals(id, updatedTagDto.getId());
        assertEquals(tagName, updatedTagDto.getName());
    }

    @Test
    @DisplayName("tag service: delete")
    void delete_returnsDeletedTagDto_whenIdIsValid() {
        when(tagDao.findById(id)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(returnedTagDto);

        TagDto deletedTagDto = tagService.delete(id);

        assertNotNull(deletedTagDto);
        assertEquals(id, deletedTagDto.getId());
        assertEquals(tagName, deletedTagDto.getName());
        verify(tagDao, times(1)).delete(tag);
    }
}
