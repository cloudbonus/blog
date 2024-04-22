package com.github.blog.dao;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
import com.github.blog.model.Tag;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@ExtendWith({SpringExtension.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {DataSourceTestConfig.class, PersistenceJPAConfig.class, DataSourceProperties.class})
public class PostDaoImplTests {

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private CommentDao commentDao;

    @Test
    @DisplayName("post: create")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql", "/db/insert-test-data-into-post-table.sql"})
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
    @DisplayName("post: update")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-post_tag-table.sql"})
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
    @DisplayName("post: delete")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-post_tag-table.sql", "/db/insert-test-data-into-comment-table.sql"})
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
    @DisplayName("post: findByLogin")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql", "/db/insert-test-data-into-post-table.sql"})
    void findAllPostsByLogin_LoginIsKvossing0_ReturnsNonEmptyList() {
        String login = "kvossing0";
        List<Post> allPostsByLogin = postDao.findAllByLogin(login);

        assertThat(allPostsByLogin).isNotEmpty();
        assertThat(allPostsByLogin).hasSize(2);
    }

    @Test
    @DisplayName("post: findByTag")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-post_tag-table.sql"})
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

}
