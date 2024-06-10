package com.github.blog.repository;

import com.github.blog.config.ContainerConfig;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.repository.filter.UserFilter;
import com.github.blog.repository.specification.UserSpecification;
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

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@SpringBootTest(classes = ContainerConfig.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = "/db/insert-test-data-into-user-table.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class UserDaoImplTests {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private static Pageable pageable;

    @BeforeAll
    public static void setUp() {
        pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id").ascending());
    }

    @Test
    @Rollback
    @DisplayName("user dao: create")
    void create_returnsUserDto_whenDataIsValid() {
        User user = new User();
        user.setUsername("login1");
        user.setEmail("temp1@test.by");
        user.setPassword("123");
        user.setUpdatedAt(OffsetDateTime.now());
        user.setCreatedAt(OffsetDateTime.now());

        Optional<Role> role = roleRepository.findByNameIgnoreCase("ROLE_USER");
        assertThat(role).isPresent();
        user.getRoles().add(role.get());

        user = userRepository.save(user);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getRoles()).isNotEmpty().hasSize(1);

        assertThat(userRepository.findById(user.getId())).isPresent();
        assertThat(userRepository.findAll()).isNotEmpty().hasSize(7);
    }

    @Test
    @Rollback
    @DisplayName("user dao: update")
    void update_returnsUpdatedUserDto_whenDataIsValid() {
        String username = "kvossing0";
        String expectedLogin = "new_kvossing0";

        UserFilter filter = new UserFilter();
        filter.setUsername(username);

        Page<User> filteredUserResult = userRepository.findAll(UserSpecification.filterBy(filter), pageable);

        assertThat(filteredUserResult.getContent()).isNotEmpty();

        User user = filteredUserResult.getContent().get(0);
        user.setUsername(expectedLogin);
        Long id = user.getId();

        user = userRepository.save(user);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getUsername()).isEqualTo(expectedLogin);
    }

    @Test
    @Rollback
    @DisplayName("user dao: delete")
    void delete_deletesUser_whenDataIsValid() {
        String username = "kvossing0";

        UserFilter filter = new UserFilter();
        filter.setUsername(username);
        Page<User> filteredUserResult = userRepository.findAll(UserSpecification.filterBy(filter), pageable);

        assertThat(filteredUserResult.getContent()).isNotEmpty();

        userRepository.delete(filteredUserResult.getContent().get(0));

        assertThat(userRepository.findAll()).isNotEmpty().hasSize(5);
    }

    @Test
    @DisplayName("user dao: find by id")
    void findById_returnsUser_whenIdIsValid() {
        Optional<User> foundUser = userRepository.findById(4L);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(4L);
        assertThat(foundUser.get().getUsername()).isEqualTo("user");
    }

    @Test
    @DisplayName("user dao: find all by role")
    void find_findsAllUsersByRole_whenDataIsValid() {
        UserFilter filter = new UserFilter();
        filter.setRoleId(2L);

        assertThat(userRepository.findAll(UserSpecification.filterBy(filter), pageable).getContent()).isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("user dao: find by username")
    void find_findsUserByUsername_whenDataIsValid() {
        String username = "kvossing0";

        UserFilter filter = new UserFilter();
        filter.setUsername(username);

        Page<User> filteredUserResult = userRepository.findAll(UserSpecification.filterBy(filter), pageable);

        assertThat(filteredUserResult.getContent()).isNotEmpty().hasSize(1);
        assertThat(filteredUserResult.getContent().get(0).getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("user dao: find all by job")
    @Sql("/db/insert-test-data-into-user_info-table.sql")
    void find_findsAllUsersByJobTitle_whenDataIsValid() {
        String jobTitle = "Software Engineer";

        UserFilter filter = new UserFilter();
        filter.setJob(jobTitle);

        assertThat(userRepository.findAll(UserSpecification.filterBy(filter), pageable).getContent()).isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("user dao: find all by university")
    @Sql("/db/insert-test-data-into-user_info-table.sql")
    void find_findsAllUsersByUniversity_whenDataIsValid() {
        String university = "MIT";

        UserFilter filter = new UserFilter();
        filter.setUniversity(university);

        assertThat(userRepository.findAll(UserSpecification.filterBy(filter), pageable).getContent()).isNotEmpty().hasSize(1);
    }
}
