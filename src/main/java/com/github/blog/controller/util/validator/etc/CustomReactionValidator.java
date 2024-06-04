package com.github.blog.controller.util.validator.etc;

import com.github.blog.controller.annotation.etc.UniqueReaction;
import com.github.blog.controller.dto.request.ReactionRequest;
import com.github.blog.repository.ReactionDao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@RequiredArgsConstructor
public class CustomReactionValidator implements ConstraintValidator<UniqueReaction, ReactionRequest> {

    private final ReactionDao reactionDao;

    @Override
    public boolean isValid(ReactionRequest request, ConstraintValidatorContext context) {
        return reactionDao.findByName(request.getName()).isEmpty();
    }
}