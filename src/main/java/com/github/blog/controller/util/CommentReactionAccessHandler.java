package com.github.blog.controller.util;

import com.github.blog.controller.dto.common.CommentReactionDto;
import com.github.blog.service.CommentReactionService;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Raman Haurylau
 */
@Component("commentReactionAccess")
@RequiredArgsConstructor
public class CommentReactionAccessHandler {

    private final AuthenticatedUserService authenticatedUserService;

    private final CommentReactionService commentReactionService;

    public boolean verifyOwnership(Long id) {
        try {
            Long sessionUserId = authenticatedUserService.getAuthenticatedUser().getId();
            CommentReactionDto commentReaction = commentReactionService.findById(id);
            return commentReaction.getUserId().equals(sessionUserId);
        } catch (CustomException e) {
            return false;
        }
    }
}
