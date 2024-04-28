package com.github.blog.dao;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface CrudDao<E, ID> {
    Optional<E> findById(final ID id);

    List<E> findAll();

    E create(final E entity);

    E update(final E entity);

    void delete(final E entity);
}
