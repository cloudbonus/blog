package com.github.blog.controller.dto.response;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public record PageResponse<T>(List<T> content, PageableResponse pageable, Long totalNumberOfEntities) {
    public int getTotalNumberOfPages() {
        return (int) Math.ceil((double) totalNumberOfEntities / pageable.pageSize());
    }
}
