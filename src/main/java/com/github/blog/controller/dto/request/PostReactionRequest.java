package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.etc.UniquePostReaction;
import com.github.blog.controller.util.marker.Marker;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PostReactionRequest {
    @NotNull(message = "Post ID is mandatory", groups = Marker.First.class)
    @Null(message = "Post ID should be null", groups = Marker.onUpdate.class)
    @UniquePostReaction(groups = Marker.Second.class)
    private Long postId;

    @NotNull(message = "Reaction ID is mandatory")
    private Long reactionId;
}
