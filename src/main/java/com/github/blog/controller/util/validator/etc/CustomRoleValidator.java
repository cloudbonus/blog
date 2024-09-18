package com.github.blog.controller.util.validator.etc;

import com.github.blog.controller.annotation.etc.UniqueRole;
import com.github.blog.controller.dto.request.RoleRequest;
import com.github.blog.repository.RoleRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@RequiredArgsConstructor
public class CustomRoleValidator implements ConstraintValidator<UniqueRole, RoleRequest> {

    private final RoleRepository roleRepository;
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public boolean isValid(RoleRequest request, ConstraintValidatorContext context) {
        return roleRepository.findByNameIgnoreCase(ROLE_PREFIX + request.name()).isEmpty();
    }
}
