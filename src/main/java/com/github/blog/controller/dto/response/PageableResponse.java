package com.github.blog.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PageableResponse {
    private int pageSize;
    private int pageNumber;
    private String orderBy;
}
