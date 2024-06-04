package com.github.blog.controller.util;

import com.github.blog.controller.dto.common.PostReactionDto;
import com.github.blog.service.PostReactionService;
import com.github.blog.service.exception.impl.CustomException;
import com.github.blog.service.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Raman Haurylau
 */
@Component("postReactionAccess")
@RequiredArgsConstructor
public class PostReactionAccessHandler {

    private final AuthenticatedUserService authenticatedUserService;

    private final PostReactionService postReactionService;

    public boolean verifyOwnership(Long id) {
        try {
            Long sessionUserId = authenticatedUserService.getAuthenticatedUser().getId();
            PostReactionDto postReaction = postReactionService.findById(id);
            return postReaction.getUserId().equals(sessionUserId);
        } catch (CustomException e) {
            return false;
        }
    }
}
