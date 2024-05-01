package com.github.blog.repository;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.repository.dto.filter.PostFilter;
import com.github.blog.repository.dto.filter.UserFilter;
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
@ContextConfiguration(classes = {DaoTestConfig.class, PersistenceJPAConfig.class, DataSourceProperties.class})
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
    @DisplayName("post dao: create")
    @Sql({"/db/daotests/insert-test-data-into-user-table.sql", "/db/daotests/insert-test-data-into-post-table.sql"})
    void create_returnsPostDto_whenDataIsValid() {
        String content = "This is the content of the first post.";
        String login = "kvossing0";

        UserFilter filter = new UserFilter();
        filter.setLogin(login);
        List<User> filteredUserResult = userDao.findAll(filter);

        assertThat(filteredUserResult).isNotEmpty();

        Post post = new Post();
        post.setUser(filteredUserResult.get(0));
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
    @DisplayName("post dao: update")
    @Sql({"/db/daotests/insert-test-data-into-user-table.sql", "/db/daotests/insert-test-data-into-post-table.sql", "/db/daotests/insert-test-data-into-post_tag-table.sql"})
    void update_returnsUpdatedPostDto_whenDataIsValid() {
        String login = "gmaccook1";
        PostFilter filter = new PostFilter();
        filter.setLogin(login);

        List<Post> filteredPostResult = postDao.findAll(filter);

        assertThat(filteredPostResult).isNotEmpty().hasSize(1);

        Post post = filteredPostResult.get(0);
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
    @DisplayName("post dao: delete")
    @Sql({"/db/daotests/insert-test-data-into-user-table.sql", "/db/daotests/insert-test-data-into-post-table.sql", "/db/daotests/insert-test-data-into-post_tag-table.sql", "/db/daotests/insert-test-data-into-comment-table.sql"})
    void delete_deletesPost_whenDataIsValid() {
        String login = "gmaccook1";
        PostFilter filter = new PostFilter();
        filter.setLogin(login);

        List<Post> filteredPostResult = postDao.findAll(filter);

        assertThat(filteredPostResult).isNotEmpty().hasSize(1);

        Post post = filteredPostResult.get(0);

        postDao.delete(post);

        List<Post> allPosts = postDao.findAll();
        List<Tag> allTags = tagDao.findAll();
        List<Comment> allComments = commentDao.findAll();

        assertThat(allPosts).isNotEmpty().hasSize(2);
        assertThat(allTags).isNotEmpty().hasSize(2);
        assertThat(allComments).isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("post dao: findAllByLogin")
    @Sql({"/db/daotests/insert-test-data-into-user-table.sql", "/db/daotests/insert-test-data-into-post-table.sql"})
    void find_findsAllPostsByLogin_whenDataIsValid() {
        String login = "kvossing0";
        PostFilter filter = new PostFilter();
        filter.setLogin(login);

        List<Post> filteredPostResult2 = postDao.findAll(filter);

        assertThat(filteredPostResult2).isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("post dao: findAllByTag")
    @Sql({"/db/daotests/insert-test-data-into-user-table.sql", "/db/daotests/insert-test-data-into-post-table.sql", "/db/daotests/insert-test-data-into-post_tag-table.sql"})
    void find_findsAllPostsByTag_whenDataIsValid() {
        String tag1 = "news";
        String tag2 = "education";
        PostFilter filter = new PostFilter();

        filter.setTag(tag1);
        List<Post> filteredPostResult1 = postDao.findAll(filter);

        filter.setTag(tag2);
        List<Post> filteredPostResult2 = postDao.findAll(filter);

        assertThat(filteredPostResult1).isNotEmpty().hasSize(3);
        assertThat(filteredPostResult2).isNotEmpty().hasSize(1);
    }
}
