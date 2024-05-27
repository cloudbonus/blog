package com.github.blog.controller.util.validator.user;

import com.github.blog.controller.annotation.user.UniqueEmail;
import com.github.blog.repository.UserDao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@RequiredArgsConstructor
public class CustomUniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserDao userDao;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context){
        if (email == null) {
            return true;
        }
        else {
            return userDao.findByEmail(email).isEmpty();
        }
    }
}
