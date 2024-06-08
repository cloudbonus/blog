package com.github.blog.controller.dto.response;

/**
 * @author Raman Haurylau
 */
public record PageableResponse(int pageSize, int pageNumber, String orderBy) {
}
