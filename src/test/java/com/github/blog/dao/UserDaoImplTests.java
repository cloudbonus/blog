package com.github.blog.dao;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.model.Role;
import com.github.blog.model.User;
import com.github.blog.model.UserDetail;
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

    @Autowired
    private UserDetailDao userDetailDao;

    @Test
    @DisplayName("user dao: create")
    @Sql("/db/insert-test-data-into-user-table.sql")
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

        List<User> allUsers = userDao.findAll();

        assertThat(allUsers).isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("user dao: update")
    @Sql("/db/insert-test-data-into-user-table.sql")
    void update_returnsUpdatedUserDto_whenDataIsValid() {
        String login = "kvossing0";
        String expectedLogin = "new_kvossing0";

        Optional<User> u = userDao.findByLogin(login);
        assertThat(u).isPresent();
        User user = u.get();
        user.setLogin("new_kvossing0");

        Long id = user.getId();

        user = userDao.update(user);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getLogin()).isEqualTo(expectedLogin);
    }

    @Test
    @DisplayName("user dao: delete")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql"})
    void delete_deletesUser_whenDataIsValid() {
        String login = "kvossing0";

        Optional<User> user = userDao.findByLogin(login);
        assertThat(user).isPresent();
        userDao.delete(user.get());

        user = userDao.findByLogin(login);

        assertThat(user).isEmpty();

        List<UserDetail> allUserDetail = userDetailDao.findAll();
        List<User> allUsers = userDao.findAll();

        assertThat(allUserDetail).isNotEmpty().hasSize(1);
        assertThat(allUsers).isNotEmpty().hasSize(1);
        assertThat(roleDao.findAll()).isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("user dao: findAllByRole")
    @Sql("/db/insert-test-data-into-user-table.sql")
    void find_findsAllUsersByRole_whenDataIsValid() {
        List<User> allUsersByRole = userDao.findAllByRole("ROLE_USER");

        assertThat(allUsersByRole).isNotEmpty();
        assertThat(allUsersByRole).hasSize(2);
    }

    @Test
    @DisplayName("user dao: findByLogin")
    @Sql("/db/insert-test-data-into-user-table.sql")
    void find_findsUserByLogin_whenDataIsValid() {
        String login = "kvossing0";
        Optional<User> user = userDao.findByLogin(login);
        assertThat(user).isPresent();
        assertThat(user.get().getLogin()).isEqualTo(login);
    }

    @Test
    @DisplayName("user dao: findAllByJobTitle")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql"})
    void find_findsAllUsersByJobTitle_whenDataIsValid() {
        String jobTitle = "Software Engineer";
        List<User> allUsersByJobTitle = userDao.findAllByJobTitle(jobTitle);

        assertThat(allUsersByJobTitle).isNotEmpty();
        assertThat(allUsersByJobTitle).hasSize(1);
    }

    @Test
    @DisplayName("user dao: findAllByUniversity")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql"})
    void find_findsAllUsersByUniversity_whenDataIsValid() {
        String university = "MIT";
        List<User> allUsersByUniversity = userDao.findAllByUniversity(university);

        assertThat(allUsersByUniversity).isNotEmpty();
        assertThat(allUsersByUniversity).hasSize(1);
    }
}
