package com.github.blog.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PageableRequest {
    private int pageSize;
    private int pageNumber;
    private String orderBy;

    public PageableRequest() {
        this.pageSize = Integer.MAX_VALUE;
        this.pageNumber = 1;
        this.orderBy = "ASC";
    }
}
