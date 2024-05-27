package com.github.blog.controller.dto.request;

import com.github.blog.controller.util.marker.Marker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PostRequest {
    @NotBlank(message = "Content is mandatory", groups = Marker.onCreate.class)
    @Size(message = "Title should be between 10 and 100", min = 10, max = 100)
    private String title;

    @NotBlank(message = "Content is mandatory", groups = Marker.onCreate.class)
    @Size(message = "Content should be between 10 and 10.000", min = 10, max = 10000)
    private String content;

    private List<@Positive Long> tagIds;
}
