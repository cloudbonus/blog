package com.github.blog.controller.util.marker;

import jakarta.validation.GroupSequence;

/**
 * @author Raman Haurylau
 */
public interface CommentValidationGroups {
    @GroupSequence({Marker.First.class, Marker.Second.class})
    interface CommentReactionCreateValidationGroupSequence {}
}
