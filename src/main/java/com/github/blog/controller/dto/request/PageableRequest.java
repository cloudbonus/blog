package com.github.blog.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PageableRequest {
    int pageSize;
    int pageNumber;

    public PageableRequest() {
        this.pageSize = Integer.MAX_VALUE;
        this.pageNumber = 1;
    }
}
