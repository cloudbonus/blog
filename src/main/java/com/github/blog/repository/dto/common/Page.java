package com.github.blog.repository.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Page<T> {
    private List<T> content;
    private Pageable pageable;
    private Long totalNumberOfEntities;

    @JsonIgnore
    public boolean isEmpty() {
        return content.isEmpty();
    }
}
