package com.github.blog.repository;

import com.github.blog.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * @author Raman Haurylau
 */
public interface RoleRepository extends CrudRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    @Override
    @NonNull
    Page<Role> findAll(@Nullable Specification<Role> spec, @NonNull Pageable pageable);

    Optional<Role> findByNameIgnoreCase(String name);
}
