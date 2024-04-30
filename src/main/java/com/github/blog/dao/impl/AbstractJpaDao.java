package com.github.blog.dao.impl;

import com.github.blog.dao.CrudDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public class AbstractJpaDao<E, ID> implements CrudDao<E, ID> {
    private final Class<E> clazz;

    @SuppressWarnings("unchecked")
    public AbstractJpaDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        clazz = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
    }

    @PersistenceContext
    protected EntityManager entityManager;

    public Optional<E> findById(ID id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @SuppressWarnings("unchecked")
    public List<E> findAll() {
        return entityManager.createQuery("from " + clazz.getSimpleName()).getResultList();
    }

    public E create(E entity) {
        entityManager.persist(entity);
        return entity;
    }

    public E update(E entity) {
        return entityManager.merge(entity);
    }

    public void delete(E entity) {
        entityManager.remove(entity);
    }
}
