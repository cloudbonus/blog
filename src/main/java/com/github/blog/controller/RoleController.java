package com.github.blog.controller;

import com.github.blog.dto.RoleDto;
import com.github.blog.service.RoleService;
import com.github.blog.util.DefaultMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@AllArgsConstructor
public class RoleController {
    private final RoleService roleService;
    private final DefaultMapper mapper;

    public String create(RoleDto roleDto) {
        return mapper.convertToJson(roleService.create(roleDto));
    }

    public String findById(int id) {
        return mapper.convertToJson(roleService.findById(id));
    }

    public String findAll() {
        List<RoleDto> roles = roleService.findAll();
        return mapper.convertToJson(roles);
    }

    public String update(int id, RoleDto roleDto) {
        return mapper.convertToJson(roleService.update(id, roleDto));
    }

    public String remove(int id) {
        int result = roleService.remove(id);
        if (result > 0)
            return String.format("Removed Successfully %d", result);
        else return "Could not remove";
    }

}

