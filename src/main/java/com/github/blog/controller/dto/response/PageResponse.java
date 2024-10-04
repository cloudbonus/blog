package com.github.blog.controller.dto.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author Raman Haurylau
 */
public record PageResponse<T>(List<T> content, int size, long totalElements, int page, int totalPages) implements Serializable {
}
