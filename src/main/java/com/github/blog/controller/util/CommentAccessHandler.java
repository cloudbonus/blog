package com.github.blog.controller.util;

import com.github.blog.controller.dto.common.CommentDto;
import com.github.blog.service.CommentService;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Raman Haurylau
 */
@Component("commentAccess")
@RequiredArgsConstructor
public class CommentAccessHandler {

    private final AuthenticatedUserService authenticatedUserService;

    private final CommentService commentService;

    public boolean verifyOwnership(Long id) {
        try {
            Long sessionUserId = authenticatedUserService.getAuthenticatedUser().getId();
            CommentDto comment = commentService.findById(id);
            return comment.getUserId().equals(sessionUserId);
        } catch (CustomException e) {
            return false;
        }
    }
}
