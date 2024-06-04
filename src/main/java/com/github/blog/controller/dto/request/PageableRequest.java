package com.github.blog.controller.dto.request;

import jakarta.validation.constraints.Pattern;
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
    @Pattern(message = "Only asc and desc supported", regexp = "^(?i)(asc|desc)$")
    private String orderBy;

    public PageableRequest() {
        this.pageSize = Integer.MAX_VALUE;
        this.pageNumber = 1;
        this.orderBy = "asc";
    }
}
