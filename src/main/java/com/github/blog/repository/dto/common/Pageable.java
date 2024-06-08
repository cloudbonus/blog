package com.github.blog.repository.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class Pageable {
    private int pageSize;
    private int pageNumber;
    private String orderBy;

    @JsonIgnore
    public int getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
