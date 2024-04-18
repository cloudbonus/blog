package com.github.blog.controller;

import com.github.blog.controller.mapper.JsonMapper;
import com.github.blog.dto.UserDetailDto;
import com.github.blog.service.UserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@AllArgsConstructor
public class UserDetailsController {
    private final UserDetailService userDetailService;
    private final JsonMapper jsonMapper;

    public String create(UserDetailDto userDetails) {
        return jsonMapper.convertToJson(userDetailService.create(userDetails));
    }

    public String findById(Long id) {
        return jsonMapper.convertToJson(userDetailService.findById(id));
    }

    public String findAll() {
        List<UserDetailDto> userDetailsDto = userDetailService.findAll();
        return jsonMapper.convertToJson(userDetailsDto);
    }

    public String update(Long id, UserDetailDto userDetailsDto) {
        return jsonMapper.convertToJson(userDetailService.update(id, userDetailsDto));
    }

    public void delete(Long id) {
        userDetailService.delete(id);
    }

}
