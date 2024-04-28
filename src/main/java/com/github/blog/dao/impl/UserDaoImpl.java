package com.github.blog.dao.impl;

import com.github.blog.dao.UserDao;
import com.github.blog.model.Role;
import com.github.blog.model.Role_;
import com.github.blog.model.User;
import com.github.blog.model.User_;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Repository
public class UserDaoImpl extends AbstractJpaDao<User, Long> implements UserDao {

    @Override
    public List<User> findAllByRole(String roleName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        Root<User> userRoot = cq.from(User.class);
        Join<User, Role> roles = userRoot.join(User_.roles);

        Predicate roleNamePredicate = cb.equal(roles.get(Role_.roleName), roleName);

        cq.select(userRoot).where(roleNamePredicate);

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<User> findAllByJobTitle(String jobTitle) {
        String jpql = "select u from User u join UserDetail ud on u.id = ud.user.id where ud.jobTitle = :jobTitle";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("jobTitle", jobTitle);
        return query.getResultList();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        Root<User> userRoot = cq.from(User.class);

        Predicate loginPredicate = cb.equal(userRoot.get(User_.login), login);

        cq.select(userRoot).where(loginPredicate);

        try {
            User user = entityManager.createQuery(cq).getSingleResult();
            return Optional.of(user);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAllByUniversity(String universityName) {
        String jpql = "select u from User u where u.id in (select ud.id from UserDetail ud where ud.universityName = :universityName)";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("universityName", universityName);
        return query.getResultList();
    }
}