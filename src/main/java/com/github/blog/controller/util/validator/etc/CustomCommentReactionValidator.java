package com.github.blog.controller.util.validator.etc;

import com.github.blog.controller.annotation.etc.UniqueCommentReaction;
import com.github.blog.controller.dto.request.CommentReactionRequest;
import com.github.blog.repository.CommentReactionRepository;
import com.github.blog.service.util.UserAccessHandler;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * @author Raman Haurylau
 */
@RequiredArgsConstructor
public class CustomCommentReactionValidator implements ConstraintValidator<UniqueCommentReaction, CommentReactionRequest> {

    private final CommentReactionRepository commentReactionRepository;

    private final UserAccessHandler userAccessHandler;

    @Override
    public boolean isValid(CommentReactionRequest commentId, ConstraintValidatorContext context) {
        if (commentId.commentId() == null) {
            return true;
        } else {
            return commentReactionRepository.findByCommentIdAndUserId(commentId.commentId(), userAccessHandler.getUserId()).isEmpty();
        }
    }
}
