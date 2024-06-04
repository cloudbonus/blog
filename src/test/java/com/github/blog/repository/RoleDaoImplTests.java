package com.github.blog.repository;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.config.RepositoryTestConfig;
import com.github.blog.config.WebTestConfig;
import com.github.blog.model.Role;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.RoleFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Raman Haurylau
 */
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringJUnitConfig(classes = {WebTestConfig.class, RepositoryTestConfig.class, PersistenceJPAConfig.class, DataSourceProperties.class})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class RoleDaoImplTests {

    @Autowired
    private RoleDao roleDao;

    private static Pageable pageable;

    @BeforeAll
    public static void setUp() {
        pageable = new Pageable();
        pageable.setPageSize(Integer.MAX_VALUE);
        pageable.setPageNumber(1);
        pageable.setOrderBy("ASC");
    }

    @Test
    @Rollback
    @DisplayName("role dao: create")
    void create_returnsRole_whenDataIsValid() {
        Role newRole = new Role();
        newRole.setName("ROLE_TEMP");

        Role createdRole = roleDao.create(newRole);

        assertThat(createdRole).isNotNull();
        assertThat(createdRole.getId()).isNotNull();
        assertThat(createdRole.getName()).isEqualTo("ROLE_TEMP");
    }


    @Test
    @DisplayName("role dao: find by id")
    void findById_returnsRole_whenIdIsValid() {
        Optional<Role> foundRole = roleDao.findById(2L);

        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getId()).isEqualTo(2L);
        assertThat(foundRole.get().getName()).isEqualTo("ROLE_USER");
    }

    @Test
    @Rollback
    @DisplayName("role dao: update")
    void update_returnsUpdatedRole_whenDataIsValid() {
        Optional<Role> optionalRole = roleDao.findById(2L);

        assertThat(optionalRole).isPresent();

        Role updatedRole = optionalRole.get();
        updatedRole.setName("Update role");

        updatedRole = roleDao.update(updatedRole);

        assertThat(updatedRole).isNotNull();
        assertThat(updatedRole.getId()).isEqualTo(2L);
        assertThat(updatedRole.getName()).isEqualTo("Update role");
    }

    @Test
    @Rollback
    @DisplayName("role dao: delete")
    void delete_deletesRole_whenIdIsValid() {
        Optional<Role> optionalRole = roleDao.findById(1L);

        assertThat(optionalRole).isPresent();

        Role deletedRole = optionalRole.get();

        roleDao.delete(deletedRole);

        assertThat(roleDao.findAll()).isNotEmpty().hasSize(3);
    }


    @Test
    @DisplayName("role dao: find all with filter and pagination")
    @Sql("/db/insert-test-data-into-user-table.sql")
    void findAll_withFilterAndPagination_returnsFilteredAndPagedRoles() {
        RoleFilter filter = new RoleFilter();
        filter.setUserId(3L);

        Page<Role> rolesPage = roleDao.findAll(filter, pageable);

        assertThat(rolesPage.getContent()).isNotEmpty().hasSize(1);
    }
}
