package com.github.blog.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.blog.repository.dto.common.Pageable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Raman Haurylau
 */
@Getter
@Setter
public class Page<T> {
    private List<T> content;
    private Pageable pageable;
    private Long totalNumberOfEntities;

    public Page(List<T> content, Pageable pageable, Long count) {
        this.content = content;
        this.pageable = pageable;
        this.totalNumberOfEntities = count;
    }

    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new Page<>(content.stream().map(converter).collect(Collectors.toList()), pageable, totalNumberOfEntities);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return content.isEmpty();
    }

    public int getTotalNumberOfPages() {
        return (int) Math.ceil((double) totalNumberOfEntities / pageable.getPageSize());
    }
}
