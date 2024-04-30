package com.github.blog.dao;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.dto.filter.UserDtoFilter;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@ExtendWith({SpringExtension.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {DaoTestConfig.class, PersistenceJPAConfig.class, DataSourceProperties.class})
public class UserDaoImplTests {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    @Test
    @DisplayName("user dao: create")
    @Sql("/db/daotests/insert-test-data-into-user-table.sql")
    void create_returnsUserDto_whenDataIsValid() {
        User user = new User();
        user.setLogin("login1");
        user.setEmail("temp1@test.by");
        user.setPassword("123");
        user.setLastLogin(OffsetDateTime.now());
        user.setCreatedAt(OffsetDateTime.now());

        Optional<Role> role = roleDao.findByName("ROLE_USER");
        assertThat(role).isPresent();
        user.getRoles().add(role.get());

        user = userDao.create(user);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getRoles()).isNotEmpty().hasSize(1);

        assertThat(userDao.findById(user.getId())).isPresent();
        assertThat(userDao.findAll()).isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("user dao: update")
    @Sql("/db/daotests/insert-test-data-into-user-table.sql")
    void update_returnsUpdatedUserDto_whenDataIsValid() {
        String login = "kvossing0";
        String expectedLogin = "new_kvossing0";

        UserDtoFilter filter = new UserDtoFilter();
        filter.setLogin(login);

        List<User> filteredUserResult = userDao.findAll(filter);

        assertThat(filteredUserResult).isNotEmpty();

        User user = filteredUserResult.get(0);
        user.setLogin(expectedLogin);
        Long id = user.getId();

        user = userDao.update(user);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getLogin()).isEqualTo(expectedLogin);
    }

    @Test
    @DisplayName("user dao: delete")
    @Sql("/db/daotests/insert-test-data-into-user-table.sql")
    void delete_deletesUser_whenDataIsValid() {
        String login = "kvossing0";

        UserDtoFilter filter = new UserDtoFilter();
        filter.setLogin(login);
        List<User> filteredUserResult = userDao.findAll(filter);

        assertThat(filteredUserResult).isNotEmpty();

        userDao.delete(filteredUserResult.get(0));

        assertThat(userDao.findAll()).isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("user dao: findAllByRole")
    @Sql("/db/daotests/insert-test-data-into-user-table.sql")
    void find_findsAllUsersByRole_whenDataIsValid() {
        String role = "user";

        UserDtoFilter filter = new UserDtoFilter();
        filter.setRole(role);

        assertThat(userDao.findAll(filter)).isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("user dao: findByLogin")
    @Sql("/db/daotests/insert-test-data-into-user-table.sql")
    void find_findsUserByLogin_whenDataIsValid() {
        String login = "kvossing0";

        UserDtoFilter filter = new UserDtoFilter();
        filter.setLogin(login);

        List<User> filteredUserResult = userDao.findAll(filter);

        assertThat(filteredUserResult).isNotEmpty().hasSize(1);
        assertThat(filteredUserResult.get(0).getLogin()).isEqualTo(login);
    }

    @Test
    @DisplayName("user dao: findAllByJobTitle")
    @Sql({"/db/daotests/insert-test-data-into-user-table.sql", "/db/daotests/insert-test-data-into-user_details-table.sql"})
    void find_findsAllUsersByJobTitle_whenDataIsValid() {
        String jobTitle = "Software Engineer";

        UserDtoFilter filter = new UserDtoFilter();
        filter.setJob(jobTitle);

        assertThat(userDao.findAll(filter)).isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("user dao: findAllByUniversity")
    @Sql({"/db/daotests/insert-test-data-into-user-table.sql", "/db/daotests/insert-test-data-into-user_details-table.sql"})
    void find_findsAllUsersByUniversity_whenDataIsValid() {
        String university = "MIT";

        UserDtoFilter filter = new UserDtoFilter();
        filter.setUniversity(university);

        assertThat(userDao.findAll(filter)).isNotEmpty().hasSize(1);
    }
}
