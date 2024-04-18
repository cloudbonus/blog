package com.github.blog.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author Raman Haurylau
 */
public class AbstractJpaDao<E, ID> {
    private final Class<E> clazz;

    @SuppressWarnings("unchecked")
    public AbstractJpaDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        clazz = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
    }

    @PersistenceContext
    protected EntityManager entityManager;

    public E findById(final ID id) {
        return entityManager.find(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public List<E> findAll() {
        return entityManager.createQuery("from " + clazz.getSimpleName()).getResultList();
    }

    public E create(final E entity) {
        entityManager.persist(entity);
        return entity;
    }

    public E update(final E entity) {
        return entityManager.merge(entity);
    }

    public void delete(final E entity) {
        entityManager.remove(entity);
    }

    public void deleteById(final ID entityId) {
        final E entity = findById(entityId);
        delete(entity);
    }
}
