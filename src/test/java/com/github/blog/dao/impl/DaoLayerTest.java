package com.github.blog.dao.impl;

import com.github.blog.config.DaoTestConfig;
import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.DataSourceTestConfig;
import com.github.blog.dao.CommentDao;
import com.github.blog.dao.PostDao;
import com.github.blog.dao.RoleDao;
import com.github.blog.dao.TagDao;
import com.github.blog.dao.UserDao;
import com.github.blog.dao.UserDetailDao;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
import com.github.blog.model.Role;
import com.github.blog.model.Tag;
import com.github.blog.model.User;
import com.github.blog.model.UserDetail;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author Raman Haurylau
 */
@Transactional
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {DataSourceTestConfig.class, DaoTestConfig.class, DataSourceProperties.class})
class DaoLayerTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDetailDao userDetailDao;
    @Autowired
    private PostDao postDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private CommentDao commentDao;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setRoleName("ROLE_USER");

        roleDao.create(role);

        User user1 = new User();
        user1.setLogin("kvossing0");
        user1.setPassword("123");
        user1.setEmail("vpenzer0@icio.us");
        user1.setCreatedAt(OffsetDateTime.now());
        user1.setLastLogin(OffsetDateTime.now());

        User user2 = new User();
        user2.setLogin("gmaccook1");
        user2.setPassword("lY3<OP4Y");
        user2.setEmail("rpucker1@statcounter.com");
        user2.setCreatedAt(OffsetDateTime.now());
        user2.setLastLogin(OffsetDateTime.now());

        user1 = userDao.create(user1);
        user2 = userDao.create(user2);

        UserDetail user1Detail = new UserDetail();
        user1Detail.setFirstname("Karl");
        user1Detail.setSurname("Doe");
        user1Detail.setUniversityName("Harvard University");
        user1Detail.setMajorName("Computer Science");
        user1Detail.setCompanyName("Google");
        user1Detail.setJobTitle("Software Engineer");
        user1Detail.setId(user1.getId());
        user1Detail.setUser(user1);

        UserDetail user2Detail = new UserDetail();
        user2Detail.setFirstname("Alice");
        user2Detail.setSurname("Johnson");
        user2Detail.setUniversityName("MIT");
        user2Detail.setMajorName("Artificial Intelligence");
        user2Detail.setCompanyName("Facebook");
        user2Detail.setJobTitle("AI Researcher");
        user2Detail.setId(user2.getId());
        user2Detail.setUser(user2);

        userDetailDao.create(user1Detail);
        userDetailDao.create(user2Detail);

        Post post1 = new Post();
        post1.setUser(user1);
        post1.setTitle("First Post");
        post1.setContent("This is the content of the first post.");
        post1.setPublishedAt(OffsetDateTime.now());

        Post post2 = new Post();
        post2.setUser(user1);
        post2.setTitle("Second Post");
        post2.setContent("This is the content of the second post.");
        post2.setPublishedAt(OffsetDateTime.now());

        Post post3 = new Post();
        post3.setUser(user2);
        post3.setTitle("Third Post");
        post3.setContent("This is the content of the third post.");
        post3.setPublishedAt(OffsetDateTime.now());

        post1 = postDao.create(post1);
        post2 = postDao.create(post2);
        post3 = postDao.create(post3);

        Tag tag1 = new Tag();
        tag1.setTagName("news");

        Tag tag2 = new Tag();
        tag2.setTagName("education");

        tag1 = tagDao.create(tag1);
        tag2 = tagDao.create(tag2);

        List<Tag> tags1 = List.of(tag1, tag2);
        List<Tag> tags2 = List.of(tag1);
        postDao.updateTags(post1, tags2);
        postDao.updateTags(post2, tags2);
        postDao.updateTags(post3, tags1);

        Comment comment1 = new Comment();
        comment1.setPost(post1);
        comment1.setUser(user1);
        comment1.setContent("This is the content of the first comment.");
        comment1.setPublishedAt(OffsetDateTime.now());

        Comment comment2 = new Comment();
        comment2.setPost(post2);
        comment2.setUser(user1);
        comment2.setContent("This is the content of the second comment.");
        comment2.setPublishedAt(OffsetDateTime.now());

        Comment comment3 = new Comment();
        comment3.setPost(post3);
        comment3.setUser(user2);
        comment3.setContent("This is the content of the third comment.");
        comment3.setPublishedAt(OffsetDateTime.now());

        commentDao.create(comment1);
        commentDao.create(comment2);
        commentDao.create(comment3);
    }

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