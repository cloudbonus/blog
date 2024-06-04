package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.etc.UniqueCommentReaction;
import com.github.blog.controller.util.marker.BaseMarker;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */

@Getter
@Setter
@UniqueCommentReaction
public class CommentReactionRequest {

    @NotNull(message = "Comment ID is mandatory", groups = BaseMarker.Create.class)
    @Null(message = "Comment ID should be null", groups = BaseMarker.Update.class)
    private Long commentId;

    @NotNull(message = "Reaction ID is mandatory")
    private Long reactionId;
}
