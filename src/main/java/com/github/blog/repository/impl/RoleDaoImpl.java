package com.github.blog.repository.impl;

import com.github.blog.repository.RoleDao;
import com.github.blog.model.Role;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Repository
@Transactional
public class RoleDaoImpl extends AbstractJpaDao<Role, Long> implements RoleDao {

    public Optional<Role> findByName(String name) {
        TypedQuery<Role> query = entityManager.createQuery("select r from Role r where r.roleName = :name", Role.class);
        query.setParameter("name", name);
        try {
            Role role = query.getSingleResult();
            return Optional.of(role);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}

