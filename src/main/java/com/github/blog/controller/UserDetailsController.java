package com.github.blog.controller;

import com.github.blog.dto.UserDetailsDto;
import com.github.blog.service.UserDetailsService;
import com.github.blog.util.DefaultMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@AllArgsConstructor
public class UserDetailsController {
    private final UserDetailsService userDetailsService;
    private final DefaultMapper mapper;

    public String create(UserDetailsDto userDetails) {
        return mapper.convertToJson(userDetailsService.create(userDetails));
    }

    public String findById(int id) {
        return mapper.convertToJson(userDetailsService.findById(id));
    }

    public String findAll() {
        List<UserDetailsDto> userDetailsDto = userDetailsService.findAll();
        return mapper.convertToJson(userDetailsDto);
    }

    public String update(int id, UserDetailsDto userDetailsDto) {
        return mapper.convertToJson(userDetailsService.update(id, userDetailsDto));
    }

    public String remove(int id) {
        int result = userDetailsService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
    }

}
