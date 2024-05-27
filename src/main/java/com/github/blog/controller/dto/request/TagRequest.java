package com.github.blog.controller.dto.request;

import com.github.blog.controller.annotation.etc.ValidAndUniqueTag;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class TagRequest {
    @ValidAndUniqueTag
    private String tagName;
}
