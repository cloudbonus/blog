package com.github.blog.repository;

import com.github.blog.config.ContainerConfig;
import com.github.blog.model.Role;
import com.github.blog.repository.filter.RoleFilter;
import com.github.blog.repository.specification.RoleSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Raman Haurylau
 */
@Transactional
@SpringBootTest(classes = ContainerConfig.class)
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class RoleDaoImplTests {

    @Autowired
    private RoleRepository roleDao;

    private static Pageable pageable;

    @BeforeAll
    public static void setUp() {
        pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id").ascending());
    }

    @Test
    @Rollback
    @DisplayName("role dao: create")
    void create_returnsRole_whenDataIsValid() {
        Role newRole = new Role();
        newRole.setName("ROLE_TEMP");

        Role createdRole = roleDao.save(newRole);

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

        updatedRole = roleDao.save(updatedRole);

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

        Page<Role> rolesPage = roleDao.findAll(RoleSpecification.filterBy(filter), pageable);

        assertThat(rolesPage.getContent()).isNotEmpty().hasSize(1);
    }
}
