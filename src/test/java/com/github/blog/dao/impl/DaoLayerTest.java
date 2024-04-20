package com.github.blog.dao.impl;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.DataSourceTestConfig;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.dao.CommentDao;
import com.github.blog.dao.PostDao;
import com.github.blog.dao.UserDao;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Raman Haurylau
 */
@Transactional
@ExtendWith({SpringExtension.class})
@Sql("classpath:db/insert-test-data.sql")
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {DataSourceTestConfig.class, PersistenceJPAConfig.class, DataSourceProperties.class})
class DaoLayerTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PostDao postDao;
    @Autowired
    private CommentDao commentDao;

    @Test
    @DisplayName("user: findAllByRole")
    void findAllUsersByRole_RoleUser_ListIsNotEmptyAndSizeIsTwo() {
        List<User> allByRole = userDao.findAllByRole("ROLE_USER");

        assertThat(allByRole).isNotEmpty();
        assertThat(allByRole).hasSize(2);
    }

    @Test
    @DisplayName("user: findByLogin")
    void findUserByLogin_Login_UserIsNotNullAndLoginIsEqual() {
        String login = "kvossing0";
        User user = userDao.findByLogin(login);

        assertThat(user).isNotNull();
        assertThat(user.getLogin()).isEqualTo(login);
    }

    @Test
    @DisplayName("user: findAllByJobTitle")
    void findAllByJobTitle_JobTitleIsSoftwareEngineer_ReturnsNonEmptyList() {
        String jobTitle = "Software Engineer";
        List<User> allByJobTitle = userDao.findAllByJobTitle(jobTitle);

        assertThat(allByJobTitle).isNotEmpty();
        assertThat(allByJobTitle).hasSize(1);
    }

    @Test
    @DisplayName("user: findAllByUniversity")
    void findAllByUniversity_UniversityIsMIT_ReturnsNonEmptyList() {
        String university = "MIT";
        List<User> allByUniversity = userDao.findAllByUniversity(university);

        assertThat(allByUniversity).isNotEmpty();
        assertThat(allByUniversity).hasSize(1);
    }

    @Test
    @DisplayName("post: findByLogin")
    void findAllByLogin_LoginIsKvossing0_ReturnsNonEmptyList() {
        String login = "kvossing0";
        List<Post> allByLogin = postDao.findAllByLogin(login);

        assertThat(allByLogin).isNotEmpty();
        assertThat(allByLogin).hasSize(2);
    }

    @Test
    @DisplayName("post: findByTag")
    void findAllByTag_TagIsNews_ReturnsNonEmptyList() {
        String tag1 = "news";
        String tag2 = "education";

        List<Post> allByTag1 = postDao.findAllByTag(tag1);
        List<Post> allByTag2 = postDao.findAllByTag(tag2);

        assertThat(allByTag1).isNotEmpty();
        assertThat(allByTag1).hasSize(3);

        assertThat(allByTag2).isNotEmpty();
        assertThat(allByTag2).hasSize(1);
    }

    @Test
    @DisplayName("comment: allByLogin")
    void findAllByLogin_GivenLogins_ReturnsNonEmptyList() {
        String login1 = "kvossing0";
        String login2 = "gmaccook1";

        List<Comment> allByLogin1 = commentDao.findAllByLogin(login1);
        List<Comment> allByLogin2 = commentDao.findAllByLogin(login2);

        assertThat(allByLogin1).isNotEmpty();
        assertThat(allByLogin1).hasSize(2);

        assertThat(allByLogin2).isNotEmpty();
        assertThat(allByLogin2).hasSize(1);
    }
}