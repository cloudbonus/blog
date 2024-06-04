package com.github.blog.controller.util.validator.etc;

import com.github.blog.controller.annotation.etc.UniqueCommentReaction;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.repository.CommentReactionDao;
import com.github.blog.service.util.UserAccessHandler;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@RequiredArgsConstructor
public class CustomCommentReactionValidator implements ConstraintValidator<UniqueCommentReaction, CommentReactionRequest> {

    private final CommentReactionDao commentReactionDao;

    private final UserAccessHandler userAccessHandler;

    @Override
    public boolean isValid(CommentReactionRequest commentId, ConstraintValidatorContext context) {
        if (commentId.getCommentId() == null) {
            return true;
        } else {
            return commentReactionDao.findByCommentIdAndUserId(commentId.getCommentId(), userAccessHandler.getUserId()).isEmpty();
        }
    }
}
