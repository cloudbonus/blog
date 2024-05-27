package com.github.blog.controller.util.validator.etc;

import com.github.blog.controller.annotation.etc.ValidAndUniqueTag;
import com.github.blog.repository.TagDao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

/**
 * @author Raman Haurylau
 */
@RequiredArgsConstructor
public class CustomTagValidator implements ConstraintValidator<ValidAndUniqueTag, String> {
    private final TagDao tagDao;

    @Override
    public boolean isValid(String tagName, ConstraintValidatorContext context) {
        return Pattern.compile("^[A-Za-z]{2,15}$")
                .matcher(tagName)
                .matches() && tagDao.findByName(tagName).isEmpty();
    }
}

