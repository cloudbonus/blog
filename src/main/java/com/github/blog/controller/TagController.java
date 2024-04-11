package com.github.blog.controller;

import com.github.blog.dto.TagDto;
import com.github.blog.service.TagService;
import com.github.blog.util.DefaultMapper;
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
    private final DefaultMapper mapper;

    public String create(TagDto tagDto) {
        return mapper.convertToJson(tagService.create(tagDto));
    }

    public String findById(int id) {
        return mapper.convertToJson(tagService.findById(id));
    }

    public String findAll() {
        List<TagDto> tags = tagService.findAll();
        return mapper.convertToJson(tags);
    }

    public String update(int id, TagDto tagDto) {
        return mapper.convertToJson(tagService.update(id, tagDto));
    }

    public String remove(int id) {
        int result = tagService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
    }

}
