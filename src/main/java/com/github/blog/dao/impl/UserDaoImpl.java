package com.github.blog.dao.impl;

import com.github.blog.dao.UserDao;
import com.github.blog.model.Role;
import com.github.blog.model.Role_;
import com.github.blog.model.User;
import com.github.blog.model.UserRole;
import com.github.blog.model.UserRoleId;
import com.github.blog.model.UserRole_;
import com.github.blog.model.User_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Raman Haurylau
 */

@Repository
public class UserDaoImpl extends AbstractJpaDao<User, Long> implements UserDao {

    @Override
    public User create(User user) {
        entityManager.persist(user);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> cq = cb.createQuery(Role.class);
        Root<Role> roleRoot = cq.from(Role.class);
        cq.select(roleRoot).where(cb.equal(roleRoot.get(Role_.roleName), "ROLE_USER"));
        Role role = entityManager.createQuery(cq).getSingleResult();

        UserRole userRole = new UserRole();
        UserRoleId id = new UserRoleId();
        id.setUserId(user.getId());
        id.setRoleId(role.getId());
        userRole.setId(id);
        userRole.setUser(user);
        userRole.setRole(role);

        entityManager.persist(userRole);
        return user;
    }

    @Override
    public List<User> findAllByRole(String roleName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        Root<UserRole> userRoleRoot = cq.from(UserRole.class);
        Root<User> userRoot = cq.from(User.class);
        Root<Role> roleRoot = cq.from(Role.class);

        Predicate userRolePredicate = cb.equal(userRoleRoot.get(UserRole_.user).get(User_.id), userRoot.get(User_.id));
        Predicate rolePredicate = cb.equal(userRoleRoot.get(UserRole_.role).get(Role_.id), roleRoot.get(Role_.id));
        Predicate roleNamePredicate = cb.equal(roleRoot.get(Role_.roleName), roleName);

        Predicate combinedPredicate = cb.and(userRolePredicate, rolePredicate, roleNamePredicate);

        cq.select(userRoot).where(combinedPredicate);

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
    public User findByLogin(String login) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        Root<User> userRoot = cq.from(User.class);

        Predicate loginPredicate = cb.equal(userRoot.get(User_.login), login);

        cq.select(userRoot).where(loginPredicate);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public List<User> findAllByUniversity(String universityName) {
        String jpql = "select u from User u where u.id in (select ud.id from UserDetail ud where ud.universityName = :universityName)";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("universityName", universityName);
        return query.getResultList();
    }
}