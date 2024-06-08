package com.github.blog.controller.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * @author Raman Haurylau
 */
public record PageableRequest(
        @Positive Integer pageSize,
        @Positive Integer pageNumber,
        @Size(min = 3, max = 4)
        @Pattern(message = "Only asc and desc supported", regexp = "^(?i)(asc|desc)$") String orderBy) {

    @Override
    public Integer pageSize() {
        return pageSize == null ? Integer.MAX_VALUE : pageSize;
    }

    @Override
    public Integer pageNumber() {
        return pageNumber == null ? 1 : pageNumber;
    }

    @Override
    public String orderBy() {
        return orderBy == null ? "asc" : orderBy;
    }
}
