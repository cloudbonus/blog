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
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author Raman Haurylau
 */
@Transactional
@ExtendWith({SpringExtension.class})
@Sql("classpath:db/insert-test-data.sql")
@TestPropertySource(locations="classpath:application-test.properties")
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
        List<User> all = userDao.findAll();
        assertEquals(2, all.size(), "The size of the list is not 2");

        List<User> allByRole = userDao.findAllByRole("ROLE_USER");

        assertFalse(allByRole.isEmpty(), "The list is empty");
        assertEquals(2, allByRole.size(), "The size of the list is not 2");
    }

    @Test
    @DisplayName("user: findByLogin")
    void findUserByLogin_Login_UserIsNotNullAndLoginIsEqual() {
        String login = "kvossing0";
        User user = userDao.findByLogin(login);

        assertNotNull(user, "User is null");
        assertEquals(login, user.getLogin(), "Logins are not equal");
    }

    @Test
    @DisplayName("user: findAllByJobTitle")
    void findAllByJobTitle_JobTitleIsSoftwareEngineer_ReturnsNonEmptyList() {
        String jobTitle = "Software Engineer";
        List<User> allByJobTitle = userDao.findAllByJobTitle(jobTitle);

        assertFalse(allByJobTitle.isEmpty(), "The list is empty");
        assertEquals(1, allByJobTitle.size(), "The size of the list is not 1");
    }

    @Test
    @DisplayName("user: findAllByUniversity")
    void findAllByUniversity_UniversityIsMIT_ReturnsNonEmptyList() {
        String university = "MIT";
        List<User> allByUniversity = userDao.findAllByUniversity(university);

        assertFalse(allByUniversity.isEmpty(), "The list is empty");
        assertEquals(1, allByUniversity.size(), "The size of the list is not 1");
    }

    @Test
    @DisplayName("post: findByLogin")
    void findAllByLogin_LoginIsKvossing0_ReturnsNonEmptyList() {
        String login = "kvossing0";
        List<Post> allByLogin = postDao.findAllByLogin(login);

        assertFalse(allByLogin.isEmpty(), "The list is empty");
        assertEquals(2, allByLogin.size(), "The size of the list is not 1");
    }

    @Test
    @DisplayName("post: findByTag")
    void findAllByTag_TagIsNews_ReturnsNonEmptyList() {
        String tag1 = "news";
        String tag2 = "education";

        List<Post> allByTag1 = postDao.findAllByTag(tag1);
        List<Post> allByTag2 = postDao.findAllByTag(tag2);

        assertFalse(allByTag1.isEmpty(), "The list is empty");
        assertEquals(3, allByTag1.size(), "The size of the list is not 1");

        assertFalse(allByTag2.isEmpty(), "The list is empty");
        assertEquals(1, allByTag2.size(), "The size of the list is not 1");
    }

    @Test
    @DisplayName("comment: allByLogin")
    void findAllByLogin_GivenLogins_ReturnsNonEmptyList() {
        String login1 = "kvossing0";
        String login2 = "gmaccook1";

        List<Comment> allByLogin1 = commentDao.findAllByLogin(login1);
        List<Comment> allByLogin2 = commentDao.findAllByLogin(login2);

        assertFalse(allByLogin1.isEmpty(), "The list is empty");
        assertEquals(2, allByLogin1.size(), "The size of the list is not 1");

        assertFalse(allByLogin2.isEmpty(), "The list is empty");
        assertEquals(1, allByLogin2.size(), "The size of the list is not 1");
    }
}