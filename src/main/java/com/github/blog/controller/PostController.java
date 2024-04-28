package com.github.blog.controller;

import com.github.blog.dto.PostDto;
import com.github.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public PostDto create(@RequestBody PostDto postDto) {
        return postService.create(postDto);
    }

    @GetMapping("{id}")
    public PostDto findById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping
    public List<PostDto> findAll() {
        return postService.findAll();
    }

    @PutMapping("{id}")
    public PostDto update(@PathVariable("id") Long id, @RequestBody PostDto postDto) {
        return postService.update(id, postDto);
    }

    @GetMapping("login")
    public List<PostDto> findAllByLogin(@RequestParam(name = "loginName") String login) {
        return postService.findAllByLogin(login);
    }

    @GetMapping("tag")
    public List<PostDto> findAllByTag(@RequestParam(name = "tagName") String tagName) {
        return postService.findAllByTag(tagName);
    }

    @DeleteMapping("{id}")
    public PostDto delete(@PathVariable("id") Long id) {
        return postService.delete(id);
    }
}
