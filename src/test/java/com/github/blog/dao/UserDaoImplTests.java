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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@ExtendWith({SpringExtension.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {DataSourceTestConfig.class, PersistenceJPAConfig.class, DataSourceProperties.class})
public class UserDaoImplTests {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserDetailDao userDetailDao;

    @Test
    @DisplayName("user: create")
    @Sql("/db/insert-test-data-into-user-table.sql")
    void createUser_twoUsers_UserListIsNotEmptyAndSizeIsTwo() {
        User user1 = new User();
        user1.setLogin("login1");
        user1.setEmail("temp1@test.by");
        user1.setPassword("123");
        user1.setLastLogin(OffsetDateTime.now());
        user1.setCreatedAt(OffsetDateTime.now());

        Role role = roleDao.findByName("ROLE_USER");
        user1.getRoles().add(role);

        user1 = userDao.create(user1);

        assertThat(user1).isNotNull();
        assertThat(user1.getId()).isNotNull();
        assertThat(user1.getRoles()).isNotEmpty().hasSize(1);

        List<User> allUsers = userDao.findAll();

        assertThat(allUsers).isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("user: update")
    @Sql("/db/insert-test-data-into-user-table.sql")
    void updateUser_LoginIsKvossing0_LoginIsUpdated() {
        String login = "kvossing0";
        String expectedLogin = "new_kvossing0";

        User user = userDao.findByLogin(login);

        user.setLogin("new_kvossing0");

        Long id = user.getId();

        user = userDao.update(user);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getLogin()).isEqualTo(expectedLogin);
    }

    @Test
    @DisplayName("user: delete")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql"})
    void deleteUser_LoginIsKvossing0_UserAndDetailsAreDeleted() {
        String login = "kvossing0";

        User user = userDao.findByLogin(login);

        userDao.deleteById(user.getId());

        user = userDao.findByLogin(login);

        assertThat(user).isNull();

        List<UserDetail> allUserDetail = userDetailDao.findAll();
        List<User> allUsers = userDao.findAll();

        assertThat(allUserDetail).isNotEmpty().hasSize(1);
        assertThat(allUsers).isNotEmpty().hasSize(1);
        assertThat(roleDao.findAll()).isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("user: findAllByRole")
    @Sql("/db/insert-test-data-into-user-table.sql")
    void findAllUsersByRole_RoleUser_ListIsNotEmptyAndSizeIsTwo() {
        List<User> allUsersByRole = userDao.findAllByRole("ROLE_USER");

        assertThat(allUsersByRole).isNotEmpty();
        assertThat(allUsersByRole).hasSize(2);
    }

    @Test
    @DisplayName("user: findByLogin")
    @Sql("/db/insert-test-data-into-user-table.sql")
    void findUserByLogin_LoginIsKvossing0_UserIsNotNullAndLoginIsEqual() {
        String login = "kvossing0";
        User user = userDao.findByLogin(login);

        assertThat(user).isNotNull();
        assertThat(user.getLogin()).isEqualTo(login);
    }

    @Test
    @DisplayName("user: findAllByJobTitle")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql"})
    void findAllUsersByJobTitle_JobTitleIsSoftwareEngineer_ReturnsNonEmptyList() {
        String jobTitle = "Software Engineer";
        List<User> allUsersByJobTitle = userDao.findAllByJobTitle(jobTitle);

        assertThat(allUsersByJobTitle).isNotEmpty();
        assertThat(allUsersByJobTitle).hasSize(1);
    }

    @Test
    @DisplayName("user: findAllByUniversity")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql"})
    void findAllUsersByUniversity_UniversityIsMIT_ReturnsNonEmptyList() {
        String university = "MIT";
        List<User> allUsersByUniversity = userDao.findAllByUniversity(university);

        assertThat(allUsersByUniversity).isNotEmpty();
        assertThat(allUsersByUniversity).hasSize(1);
    }

}
