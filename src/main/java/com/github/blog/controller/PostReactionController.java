package com.github.blog.controller;

import com.github.blog.dto.PostReactionDto;
import com.github.blog.service.PostReactionService;
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
@RequestMapping("postreactions")
@RequiredArgsConstructor
public class PostReactionController {
    private final PostReactionService postReactionService;

    @PostMapping
    public PostReactionDto create(@RequestBody PostReactionDto postReactionDto) {
        return postReactionService.create(postReactionDto);
    }

    @GetMapping("{id}")
    public PostReactionDto findById(@PathVariable("id") Long id) {
        return postReactionService.findById(id);
    }

    @GetMapping
    public List<PostReactionDto> findAll() {
        return postReactionService.findAll();
    }

    @PutMapping("{id}")
    public PostReactionDto update(@PathVariable("id") Long id, @RequestBody PostReactionDto postReactionDto) {
        return postReactionService.update(id, postReactionDto);
    }


    @DeleteMapping("{id}")
    public PostReactionDto delete(@PathVariable("id") Long id) {
        return postReactionService.delete(id);
    }
}
