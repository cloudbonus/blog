package com.github.blog.controller;

import com.github.blog.controller.mapper.JsonMapper;
import com.github.blog.dto.RoleDto;
import com.github.blog.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Controller
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    private final JsonMapper jsonMapper;

    public String create(RoleDto roleDto) {
        return jsonMapper.convertToJson(roleService.create(roleDto));
    }

    public String findById(Long id) {
        return jsonMapper.convertToJson(roleService.findById(id));
    }

    public String findAll() {
        List<RoleDto> roles = roleService.findAll();
        return jsonMapper.convertToJson(roles);
    }

    public String update(Long id, RoleDto roleDto) {
        return jsonMapper.convertToJson(roleService.update(id, roleDto));
    }

    public void delete(Long id) {
        roleService.delete(id);
    }
}

