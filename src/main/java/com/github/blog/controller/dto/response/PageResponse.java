package com.github.blog.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private PageableResponse pageable;
    private Long totalNumberOfEntities;

    public int getTotalNumberOfPages() {
        return (int) Math.ceil((double) totalNumberOfEntities / pageable.getPageSize());
    }
}
