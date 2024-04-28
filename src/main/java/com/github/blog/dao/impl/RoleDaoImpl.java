package com.github.blog.dao.impl;

import com.github.blog.dao.RoleDao;
import com.github.blog.model.Role;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Repository
public class RoleDaoImpl extends AbstractJpaDao<Role, Long> implements RoleDao {

    public Optional<Role> findByName(String name) {
        TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role r WHERE r.roleName = :name", Role.class);
        query.setParameter("name", name);
        try {
            Role role = query.getSingleResult();
            return Optional.of(role);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}

