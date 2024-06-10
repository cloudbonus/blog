package com.github.blog.repository;

import com.github.blog.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author Raman Haurylau
 */
public interface UserInfoRepository extends CrudRepository<UserInfo, Long>, JpaSpecificationExecutor<UserInfo> {
    @Override
    @NonNull
    Page<UserInfo> findAll(@Nullable Specification<UserInfo> spec, @NonNull Pageable pageable);

    List<UserInfo> findByState(String stateOne);
}
