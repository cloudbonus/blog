package com.github.blog.controller;

import com.github.blog.controller.mapper.JsonMapper;
import com.github.blog.dto.TagDto;
import com.github.blog.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@AllArgsConstructor
public class TagController {
    private final TagService tagService;
    private final JsonMapper jsonMapper;

    public String create(TagDto tagDto) {
        return jsonMapper.convertToJson(tagService.create(tagDto));
    }

    public String findById(Long id) {
        return jsonMapper.convertToJson(tagService.findById(id));
    }

    public String findAll() {
        List<TagDto> tags = tagService.findAll();
        return jsonMapper.convertToJson(tags);
    }

    public String update(Long id, TagDto tagDto) {
        return jsonMapper.convertToJson(tagService.update(id, tagDto));
    }

    public void delete(Long id) {
        tagService.delete(id);
    }
}
