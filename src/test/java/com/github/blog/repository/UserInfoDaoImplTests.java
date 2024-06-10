package com.github.blog.repository;

import com.github.blog.config.ContainerConfig;
import com.github.blog.model.User;
import com.github.blog.model.UserInfo;
import com.github.blog.repository.filter.UserInfoFilter;
import com.github.blog.repository.specification.UserInfoSpecification;
import com.github.blog.service.statemachine.state.UserInfoState;
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
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_info-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class UserInfoDaoImplTests {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserRepository userRepository;

    private static Pageable pageable;

    @BeforeAll
    public static void setUp() {
        pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id").ascending());
    }

    @Test
    @Rollback
    @DisplayName("user info dao: create")
    void create_returnsUserInfo_whenDataIsValid() {
        Optional<User> optionalUser = userRepository.findById(3L);
        assertThat(optionalUser).isPresent();
        User user = optionalUser.get();

        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUser(user);
        newUserInfo.setState(UserInfoState.RESERVED.name());

        UserInfo createdUserInfo = userInfoRepository.save(newUserInfo);

        assertThat(createdUserInfo).isNotNull();
        assertThat(createdUserInfo.getId()).isNotNull();
        assertThat(createdUserInfo.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("user info dao: find by id")
    void findById_returnsUserInfo_whenIdIsValid() {
        Optional<UserInfo> foundUserInfo = userInfoRepository.findById(1L);

        assertThat(foundUserInfo).isPresent();
        assertThat(foundUserInfo.get().getId()).isEqualTo(1L);
    }

    @Test
    @Rollback
    @DisplayName("user info dao: update")
    void update_returnsUpdatedUserInfo_whenDataIsValid() {
        Optional<UserInfo> optionalUserInfo = userInfoRepository.findById(1L);
        assertThat(optionalUserInfo).isPresent();
        UserInfo updatedUserInfo = optionalUserInfo.get();

        updatedUserInfo.setState(UserInfoState.VERIFIED.name());

        updatedUserInfo = userInfoRepository.save(updatedUserInfo);

        assertThat(updatedUserInfo).isNotNull();
        assertThat(updatedUserInfo.getId()).isEqualTo(1L);
        assertThat(updatedUserInfo.getState()).isEqualTo(UserInfoState.VERIFIED.name());
    }

    @Test
    @Rollback
    @DisplayName("user info dao: delete")
    void delete_deletesUserInfo_whenIdIsValid() {
        Optional<UserInfo> optionalUserInfo = userInfoRepository.findById(1L);
        assertThat(optionalUserInfo).isPresent();

        UserInfo deletedUserInfo = optionalUserInfo.get();

        userInfoRepository.delete(deletedUserInfo);

        assertThat(userInfoRepository.findById(1L)).isNotPresent();
    }

    @Test
    @DisplayName("user info dao: find all with filter and pagination")
    void findAll_withFilterAndPagination_returnsFilteredAndPagedUserInfos() {
        UserInfoFilter filter = new UserInfoFilter();
        Page<UserInfo> userInfosPage = userInfoRepository.findAll(UserInfoSpecification.filterBy(filter), pageable);

        assertThat(userInfosPage.getContent()).isNotEmpty();
    }
}
