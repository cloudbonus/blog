package com.github.blog.controller.dto.response;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public record PageResponse<T>(List<T> content, int size, long totalElements, int page, int totalPages) {
}
