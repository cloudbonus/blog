package com.github.blog.controller.util.validator.etc;

import com.github.blog.controller.annotation.etc.ValidAndUniqueReaction;
import com.github.blog.repository.ReactionDao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

/**
 * @author Raman Haurylau
 */
@RequiredArgsConstructor
public class CustomReactionValidator implements ConstraintValidator<ValidAndUniqueReaction, String> {
    private final ReactionDao reactionDao;

    @Override
    public boolean isValid(String reactionName, ConstraintValidatorContext context) {
        return Pattern.compile("^[A-Za-z]{2,15}$")
                .matcher(reactionName)
                .matches() && reactionDao.findByName(reactionName).isEmpty();
    }
}