package com.github.blog.controller;

import com.github.blog.controller.dto.common.TagDto;
import com.github.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@RestController
@RequestMapping("tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    public TagDto create(@RequestBody TagDto tagDto) {
        return tagService.create(tagDto);
    }

    @GetMapping("{id}")
    public TagDto findById(@PathVariable("id") Long id) {
        return tagService.findById(id);
    }

    @GetMapping
    public List<TagDto> findAll() {
        return tagService.findAll();
    }

    @PutMapping("{id}")
    public TagDto update(@PathVariable("id") Long id, @RequestBody TagDto tagDto) {
        return tagService.update(id, tagDto);
    }

    @DeleteMapping("{id}")
    public TagDto delete(@PathVariable("id") Long id) {
        return tagService.delete(id);
    }
}
