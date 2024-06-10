package com.github.blog.service;

import com.github.blog.controller.dto.common.TagDto;
import com.github.blog.controller.dto.request.PageableRequest;
import com.github.blog.controller.dto.request.TagRequest;
import com.github.blog.controller.dto.request.filter.TagFilterRequest;
import com.github.blog.controller.dto.response.PageResponse;
import com.github.blog.model.Tag;
import com.github.blog.repository.TagRepository;
import com.github.blog.repository.filter.TagFilter;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.impl.TagServiceImpl;
import com.github.blog.service.mapper.TagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private TagRequest request;
    private TagDto returnedTagDto;
    private Tag tag;

    private final Long id = 1L;
    private final String tagName = "news";

    private final PageableRequest pageableRequest = new PageableRequest(10, null, "asc");
    private final TagFilterRequest tagFilterRequest = new TagFilterRequest(null);

    @BeforeEach
    void setUp() {
        request = new TagRequest(tagName);

        tag = new Tag();
        tag.setId(id);
        tag.setName(tagName);

        returnedTagDto = new TagDto(id, tagName);
    }

    @Test
    @DisplayName("tag service: create")
    void create_returnsTagDto_whenDataIsValid() {
        when(tagMapper.toEntity(request)).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(tag);
        when(tagMapper.toDto(tag)).thenReturn(returnedTagDto);

        TagDto createdTagDto = tagService.create(request);

        assertNotNull(createdTagDto);
        assertEquals(id, createdTagDto.id());
        assertEquals(tagName, createdTagDto.name());
    }

    @Test
    @DisplayName("tag service: find by id")
    void findById_returnsTagDto_whenIdIsValid() {
        when(tagRepository.findById(id)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(returnedTagDto);

        TagDto foundTagDto = tagService.findById(id);

        assertNotNull(foundTagDto);
        assertEquals(id, foundTagDto.id());
        assertEquals(tagName, foundTagDto.name());
    }

    @Test
    @DisplayName("tag service: find by id - not found exception")
    void findById_throwsException_whenIdIsInvalid() {
        when(tagRepository.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> tagService.findById(id));

        assertEquals(ExceptionEnum.TAG_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("tag service: find all")
    void findAll_returnsPageResponse_whenDataIsValid() {
        TagFilter filter = new TagFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<Tag> page = new PageImpl<>(Collections.singletonList(tag), pageable, 1L);
        PageResponse<TagDto> pageResponse = new PageResponse<>(Collections.singletonList(returnedTagDto), 1, 1, 0, 1);

        when(tagMapper.toEntity(any(TagFilterRequest.class))).thenReturn(filter);
        when(tagRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(tagMapper.toDto(page)).thenReturn(pageResponse);

        PageResponse<TagDto> foundTags = tagService.findAll(tagFilterRequest, pageableRequest);

        assertNotNull(foundTags);
        assertEquals(1, foundTags.content().size());
        assertEquals(id, foundTags.content().get(0).id());
        assertEquals(tagName, foundTags.content().get(0).name());
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("tag service: find all - not found exception")
    void findAll_throwsException_whenNoTagsFound() {
        TagFilter filter = new TagFilter();

        Pageable pageable = PageRequest.of(pageableRequest.pageNumber(), pageableRequest.pageSize(), pageableRequest.getSort());
        Page<Tag> page = new PageImpl<>(Collections.emptyList(), pageable, 1L);

        when(tagMapper.toEntity(any(TagFilterRequest.class))).thenReturn(filter);
        when(tagRepository.findAll(any(Specification.class),  any(Pageable.class))).thenReturn(page);

        CustomException exception = assertThrows(CustomException.class, () -> tagService.findAll(tagFilterRequest, pageableRequest));

        assertEquals(ExceptionEnum.TAGS_NOT_FOUND, exception.getExceptionEnum());
    }

    @Test
    @DisplayName("tag service: update")
    void update_returnsUpdatedTagDto_whenDataIsValid() {
        when(tagRepository.findById(id)).thenReturn(Optional.of(tag));
        when(tagMapper.partialUpdate(request, tag)).thenReturn(tag);
        when(tagMapper.toDto(tag)).thenReturn(returnedTagDto);

        TagDto updatedTagDto = tagService.update(id, request);

        assertNotNull(updatedTagDto);
        assertEquals(id, updatedTagDto.id());
        assertEquals(tagName, updatedTagDto.name());
    }

    @Test
    @DisplayName("tag service: delete")
    void delete_returnsDeletedTagDto_whenIdIsValid() {
        when(tagRepository.findById(id)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(tag)).thenReturn(returnedTagDto);

        TagDto deletedTagDto = tagService.delete(id);

        assertNotNull(deletedTagDto);
        assertEquals(id, deletedTagDto.id());
        assertEquals(tagName, deletedTagDto.name());
        verify(tagRepository, times(1)).delete(tag);
    }
}
