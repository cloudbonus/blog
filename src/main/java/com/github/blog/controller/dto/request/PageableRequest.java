package com.github.blog.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Sort;

/**
 * @author Raman Haurylau
 */
public record PageableRequest(
        @Positive Integer pageSize,
        @PositiveOrZero Integer pageNumber,
        @Size(min = 3, max = 4)
        @Pattern(message = "Only asc and desc supported", regexp = "^(?i)(asc|desc)$") String orderBy) {

    private static final String SORT_BY = "id";

    @Override
    public Integer pageSize() {
        return pageSize == null ? Integer.MAX_VALUE : pageSize;
    }

    @Override
    public Integer pageNumber() {
        return pageNumber == null ? 0 : pageNumber;
    }

    @Override
    public String orderBy() {
        return orderBy == null ? "asc" : orderBy;
    }

    @JsonIgnore
    public Sort getSort() {
        return orderBy().equalsIgnoreCase("desc") ? Sort.by(SORT_BY).descending() : Sort.by(SORT_BY).ascending();
    }

}
