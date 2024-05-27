package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.etc.ValidAndUniqueReaction;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class ReactionRequest {
    @ValidAndUniqueReaction
    private String reactionName;
}
