package com.github.blog.dao;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
import com.github.blog.model.Role;
import com.github.blog.model.Tag;
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
class DaoLayerTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PostDao postDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDetailDao userDetailDao;
    @Autowired
    private TagDao tagDao;

    @Test
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("user: create")
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
    }

    @Test
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("user: update")
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
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("user: delete")
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
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("post: create")
    void createPost_LoginIsKvossing0_PostIsNotNullAndIdIsNotNull() {
        String login = "kvossing0";

        String content = "This is the content of the first post.";
        User user = userDao.findByLogin(login);
        Post post = new Post();
        post.setUser(user);
        post.setTitle("First Post");
        post.setContent(content);
        post.setPublishedAt(OffsetDateTime.now());

        post = postDao.create(post);

        assertThat(post).isNotNull();
        assertThat(post.getId()).isNotNull();
        assertThat(post.getContent()).isEqualTo(content);

        List<Post> allPosts = postDao.findAll();

        assertThat(allPosts).isNotEmpty().hasSize(4);
    }

    @Test
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("post: update")
    void updatePost_LoginIsGmaccook1_ContentIsUpdated() {
        String login = "gmaccook1";

        List<Post> allPostsByLogin = postDao.findAllByLogin(login);

        assertThat(allPostsByLogin).isNotEmpty().hasSize(1);

        Post post = allPostsByLogin.get(0);
        String updatedContent = "Look at me i've made it!!!";
        OffsetDateTime updatedTime = OffsetDateTime.now();

        post.setContent(updatedContent);
        post.setPublishedAt(updatedTime);

        post = postDao.update(post);

        assertThat(post).isNotNull();
        assertThat(post.getContent()).isEqualTo(updatedContent);
        assertThat(post.getPublishedAt()).isEqualTo(updatedTime);
        assertThat(post.getTags()).hasSize(2);
    }

    @Test
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("post: delete")
    void deletePost_LoginIsGmaccook1_PostAndCommentsAreDeleted() {
        String login = "gmaccook1";

        List<Post> allPostsByLogin = postDao.findAllByLogin(login);

        assertThat(allPostsByLogin).isNotEmpty().hasSize(1);

        Post post = allPostsByLogin.get(0);

        postDao.deleteById(post.getId());

        List<Post> allPosts = postDao.findAll();
        assertThat(allPosts).isNotEmpty().hasSize(2);

        List<Tag> allTags = tagDao.findAll();
        assertThat(allTags).isNotEmpty().hasSize(2);

        List<Comment> allComments = commentDao.findAll();
        assertThat(allComments).isNotEmpty().hasSize(2);
    }

    @Test
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("user: findAllByRole")
    void findAllUsersByRole_RoleUser_ListIsNotEmptyAndSizeIsTwo() {
        List<User> allUsersByRole = userDao.findAllByRole("ROLE_USER");

        assertThat(allUsersByRole).isNotEmpty();
        assertThat(allUsersByRole).hasSize(2);
    }

    @Test
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("user: findByLogin")
    void findUserByLogin_LoginIsKvossing0_UserIsNotNullAndLoginIsEqual() {
        String login = "kvossing0";
        User user = userDao.findByLogin(login);

        assertThat(user).isNotNull();
        assertThat(user.getLogin()).isEqualTo(login);
    }

    @Test
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("user: findAllByJobTitle")
    void findAllUsersByJobTitle_JobTitleIsSoftwareEngineer_ReturnsNonEmptyList() {
        String jobTitle = "Software Engineer";
        List<User> allUsersByJobTitle = userDao.findAllByJobTitle(jobTitle);

        assertThat(allUsersByJobTitle).isNotEmpty();
        assertThat(allUsersByJobTitle).hasSize(1);
    }

    @Test
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("user: findAllByUniversity")
    void findAllUsersByUniversity_UniversityIsMIT_ReturnsNonEmptyList() {
        String university = "MIT";
        List<User> allUsersByUniversity = userDao.findAllByUniversity(university);

        assertThat(allUsersByUniversity).isNotEmpty();
        assertThat(allUsersByUniversity).hasSize(1);
    }

    @Test
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("post: findByLogin")
    void findAllPostsByLogin_LoginIsKvossing0_ReturnsNonEmptyList() {
        String login = "kvossing0";
        List<Post> allPostsByLogin = postDao.findAllByLogin(login);

        assertThat(allPostsByLogin).isNotEmpty();
        assertThat(allPostsByLogin).hasSize(2);
    }

    @Test
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("post: findByTag")
    void findAllPostsByTag_TagIsNews_ReturnsNonEmptyList() {
        String tag1 = "news";
        String tag2 = "education";

        List<Post> allPostsByTag1 = postDao.findAllByTag(tag1);

        assertThat(allPostsByTag1).isNotEmpty();
        assertThat(allPostsByTag1).hasSize(3);

        List<Post> allPostsByTag2 = postDao.findAllByTag(tag2);

        assertThat(allPostsByTag2).isNotEmpty();
        assertThat(allPostsByTag2).hasSize(1);
    }

    @Test
    @Sql("classpath:db/insert-test-data.sql")
    @DisplayName("comment: allByLogin")
    void findAllCommentsByLogin_GivenLogins_ReturnsNonEmptyList() {
        String login1 = "kvossing0";
        String login2 = "gmaccook1";

        List<Comment> allCommentsByLogin1 = commentDao.findAllByLogin(login1);

        assertThat(allCommentsByLogin1).isNotEmpty();
        assertThat(allCommentsByLogin1).hasSize(2);

        List<Comment> allCommentsByLogin2 = commentDao.findAllByLogin(login2);

        assertThat(allCommentsByLogin2).isNotEmpty();
        assertThat(allCommentsByLogin2).hasSize(1);
    }
}