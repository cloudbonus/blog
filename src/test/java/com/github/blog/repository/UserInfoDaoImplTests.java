package com.github.blog.repository;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.config.RepositoryTestConfig;
import com.github.blog.config.WebTestConfig;
import com.github.blog.model.User;
import com.github.blog.model.UserInfo;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.UserInfoFilter;
import com.github.blog.service.statemachine.state.UserInfoState;
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
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_info-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class UserInfoDaoImplTests {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserDao userDao;

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
    @DisplayName("user info dao: create")
    void create_returnsUserInfo_whenDataIsValid() {
        Optional<User> optionalUser = userDao.findById(3L);
        assertThat(optionalUser).isPresent();
        User user = optionalUser.get();

        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUser(user);
        newUserInfo.setState(UserInfoState.RESERVED.name());

        UserInfo createdUserInfo = userInfoDao.create(newUserInfo);

        assertThat(createdUserInfo).isNotNull();
        assertThat(createdUserInfo.getId()).isNotNull();
        assertThat(createdUserInfo.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("user info dao: find by id")
    void findById_returnsUserInfo_whenIdIsValid() {
        Optional<UserInfo> foundUserInfo = userInfoDao.findById(1L);

        assertThat(foundUserInfo).isPresent();
        assertThat(foundUserInfo.get().getId()).isEqualTo(1L);
    }

    @Test
    @Rollback
    @DisplayName("user info dao: update")
    void update_returnsUpdatedUserInfo_whenDataIsValid() {
        Optional<UserInfo> optionalUserInfo = userInfoDao.findById(1L);
        assertThat(optionalUserInfo).isPresent();
        UserInfo updatedUserInfo = optionalUserInfo.get();

        updatedUserInfo.setState(UserInfoState.VERIFIED.name());

        updatedUserInfo = userInfoDao.create(updatedUserInfo);

        assertThat(updatedUserInfo).isNotNull();
        assertThat(updatedUserInfo.getId()).isEqualTo(1L);
        assertThat(updatedUserInfo.getState()).isEqualTo(UserInfoState.VERIFIED.name());
    }

    @Test
    @Rollback
    @DisplayName("user info dao: delete")
    void delete_deletesUserInfo_whenIdIsValid() {
        Optional<UserInfo> optionalUserInfo = userInfoDao.findById(1L);
        assertThat(optionalUserInfo).isPresent();

        UserInfo deletedUserInfo = optionalUserInfo.get();

        userInfoDao.delete(deletedUserInfo);

        assertThat(userInfoDao.findById(1L)).isNotPresent();
    }

    @Test
    @DisplayName("user info dao: find all with filter and pagination")
    void findAll_withFilterAndPagination_returnsFilteredAndPagedUserInfos() {
        Page<UserInfo> userInfosPage = userInfoDao.findAll(new UserInfoFilter(), pageable);

        assertThat(userInfosPage.getContent()).isNotEmpty();
    }
}
