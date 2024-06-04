package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.etc.UniquePostReaction;
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
@UniquePostReaction
public class PostReactionRequest {

    @NotNull(message = "Post ID is mandatory", groups = BaseMarker.Create.class)
    @Null(message = "Post ID should be null", groups = BaseMarker.Update.class)
    private Long postId;

    @NotNull(message = "Reaction ID is mandatory")
    private Long reactionId;
}
