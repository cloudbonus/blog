package com.github.blog.repository;

import com.github.blog.model.Tag;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.TagFilter;

import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface TagDao extends CrudDao<Tag, Long> {
    Page<Tag> findAll(TagFilter filter, Pageable pageable);
    Optional<Tag> findByName(String tagName);

}
