package com.github.blog.controller.util.validator.etc;

import com.github.blog.controller.annotation.etc.UniquePostReaction;
import com.github.blog.repository.PostReactionDao;
import com.github.blog.service.util.UserAccessHandler;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@RequiredArgsConstructor
public class CustomPostReactionValidator implements ConstraintValidator<UniquePostReaction, Long> {
    private final PostReactionDao postReactionDao;
    private final UserAccessHandler userAccessHandler;

    @Override
    public boolean isValid(Long postId, ConstraintValidatorContext context) {
        return postReactionDao.findByPostIdAndUserId(postId, userAccessHandler.getUserId()).isEmpty();
    }
}