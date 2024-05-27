package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.etc.ValidAndUniqueRole;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class RoleRequest {
    @ValidAndUniqueRole
    private String roleName;
}
