package com.github.blog.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class Pageable {
    int pageSize;
    int pageNumber;

    @JsonIgnore
    public int getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
