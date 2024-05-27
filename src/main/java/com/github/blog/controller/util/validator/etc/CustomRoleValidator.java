package com.github.blog.controller.util.validator.etc;

import com.github.blog.controller.annotation.etc.ValidAndUniqueRole;
import com.github.blog.repository.RoleDao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

/**
 * @author Raman Haurylau
 */
@RequiredArgsConstructor
public class CustomRoleValidator implements ConstraintValidator<ValidAndUniqueRole, String> {
    private final RoleDao roleDao;

    @Override
    public boolean isValid(String roleName, ConstraintValidatorContext context) {
        return Pattern.compile("^[A-Za-z]{2,15}$")
                .matcher(roleName)
                .matches() && roleDao.findByName("ROLE_" + roleName).isEmpty();
    }
}
