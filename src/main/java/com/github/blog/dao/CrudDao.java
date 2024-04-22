package com.github.blog.dao;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface CrudDao<E, ID> {
    E findById(final ID id);

    List<E> findAll();

    E create(final E entity);

    E update(final E entity);

    void delete(final E entity);

    void deleteById(final ID entityId);
}
