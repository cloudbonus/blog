package com.github.blog.controller.util.validator.user;

import com.github.blog.controller.annotation.user.UniqueUsername;
import com.github.blog.repository.UserDao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@RequiredArgsConstructor
public class CustomUniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    private final UserDao userDao;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context){
        if (username == null) {
            return true;
        }
        else {
            return userDao.findByUsername(username).isEmpty();
        }
    }
}
