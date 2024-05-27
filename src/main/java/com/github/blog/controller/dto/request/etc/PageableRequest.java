package com.github.blog.controller.dto.request.etc;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class PageableRequest {
    @Positive
    private int pageSize;
    @Positive
    private int pageNumber;
    @Size(min = 3, max = 4)
    private String orderBy;

    public PageableRequest() {
        this.pageSize = Integer.MAX_VALUE;
        this.pageNumber = 1;
        this.orderBy = "ASC";
    }
}
