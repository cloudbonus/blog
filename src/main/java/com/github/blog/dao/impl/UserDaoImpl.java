package com.github.blog.dao.impl;

import com.github.blog.dao.UserDao;
import com.github.blog.dto.filter.UserDtoFilter;
import com.github.blog.model.Role;
import com.github.blog.model.Role_;
import com.github.blog.model.User;
import com.github.blog.model.UserDetail;
import com.github.blog.model.UserDetail_;
import com.github.blog.model.User_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Repository
public class UserDaoImpl extends AbstractJpaDao<User, Long> implements UserDao {

    @Override
    public List<User> findAll(UserDtoFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
        Join<User, UserDetail> userDetail = user.join(User_.userDetail, JoinType.LEFT);
        Join<User, Role> role = user.join(User_.roles);

        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(filter.getFirstname())) {
            predicates.add(cb.like(cb.lower(userDetail.get(UserDetail_.firstname).as(String.class)), filter.getFirstname().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getSurname())) {
            predicates.add(cb.like(cb.lower(userDetail.get(UserDetail_.surname).as(String.class)), filter.getSurname().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getUniversity())) {
            predicates.add(cb.like(cb.lower(userDetail.get(UserDetail_.universityName).as(String.class)), filter.getUniversity().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getMajor())) {
            predicates.add(cb.like(cb.lower(userDetail.get(UserDetail_.majorName).as(String.class)), filter.getMajor().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getCompany())) {
            predicates.add(cb.like(cb.lower(userDetail.get(UserDetail_.companyName).as(String.class)), filter.getCompany().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getJob())) {
            predicates.add(cb.like(cb.lower(userDetail.get(UserDetail_.jobTitle).as(String.class)), filter.getJob().toLowerCase().concat("%")));
        }

        if (!ObjectUtils.isEmpty(filter.getLogin())) {
            predicates.add(cb.equal(cb.lower(user.get(User_.login).as(String.class)), filter.getLogin().toLowerCase()));
        }

        if (!ObjectUtils.isEmpty(filter.getRole())) {
            String updatedRoleName = "role_" + filter.getRole();
            predicates.add(cb.equal(cb.lower(role.get(Role_.roleName).as(String.class)), updatedRoleName.toLowerCase()));
        }

        cq.orderBy(cb.asc(user.get(User_.id)));

        cq.where(cb.and(predicates.toArray(Predicate[]::new)));
        TypedQuery<User> query = entityManager.createQuery(cq.select(user).distinct(true));

        return query.getResultList();
    }
}