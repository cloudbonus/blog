package com.github.blog.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class Page<T> {
    List<T> content;
    int pageNumber;
    int pageSize;
    int totalResults;
}
