package com.github.blog.controller.dto.request;

import com.github.blog.controller.util.marker.BaseMarker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public record PostRequest(
        @NotBlank(message = "Content is mandatory", groups = BaseMarker.Create.class) @Size(message = "Title should be between 10 and 100", min = 10, max = 100) String title,
        @NotBlank(message = "Content is mandatory", groups = BaseMarker.Create.class) @Size(message = "Content should be between 10 and 10.000", min = 10, max = 10000) String content,
        List<@Positive Long> tagIds) {
}
