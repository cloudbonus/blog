package com.github.blog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public abstract class GenericFilter {
    private int pageSize;
    private int pageNumber;

    public GenericFilter() {
        this.pageSize = Integer.MAX_VALUE;
    }
}
