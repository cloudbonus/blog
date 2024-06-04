package com.github.blog.repository;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.config.RepositoryTestConfig;
import com.github.blog.config.WebTestConfig;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.PostFilter;
import com.github.blog.repository.dto.filter.UserFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringJUnitConfig(classes = {WebTestConfig.class, RepositoryTestConfig.class, PersistenceJPAConfig.class, DataSourceProperties.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-post-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
class PostDaoImplTests {

    @Autowired
    private PostDao postDao;

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
    @DisplayName("post dao: create")
    void create_returnsPostDto_whenDataIsValid() {
        String content = "This is the content of the first post.";
        String username = "kvossing0";

        UserFilter filter = new UserFilter();
        filter.setUsername(username);

        Page<User> filteredUserResult = userDao.findAll(filter, pageable);

        assertThat(filteredUserResult.getContent()).isNotEmpty();

        Post post = new Post();
        post.setUser(filteredUserResult.getContent().get(0));
        post.setTitle("First Post");
        post.setContent(content);
        post.setCreatedAt(OffsetDateTime.now());

        post = postDao.create(post);

        assertThat(post).isNotNull();
        assertThat(post.getId()).isNotNull();
        assertThat(post.getContent()).isEqualTo(content);

        List<Post> allPosts = postDao.findAll();

        assertThat(allPosts).isNotEmpty().hasSize(8);
    }

    @Test
    @Rollback
    @DisplayName("post dao: update")
    @Sql("/db/insert-test-data-into-post_tag-table.sql")
    void update_returnsUpdatedPostDto_whenDataIsValid() {
        String username = "student";
        PostFilter filter = new PostFilter();
        filter.setUsername(username);

        Page<Post> filteredPostResult = postDao.findAll(filter, pageable);

        assertThat(filteredPostResult.getContent()).isNotEmpty().hasSize(3);

        Post post = filteredPostResult.getContent().get(0);
        String updatedContent = "Look at me i've made it!!!";

        post.setContent(updatedContent);

        post = postDao.update(post);

        assertThat(post).isNotNull();
        assertThat(post.getContent()).isEqualTo(updatedContent);
        assertThat(post.getTags()).hasSize(1);
    }

    @Test
    @Rollback
    @DisplayName("post dao: delete")
    void delete_deletesPost_whenDataIsValid() {
        Optional<Post> post = postDao.findById(1L);

        assertThat(post).isPresent();

        postDao.delete(post.get());

        assertThat(postDao.findAll()).isNotEmpty().hasSize(6);
    }

    @Test
    @DisplayName("post dao: find by id")
    void findById_returnsPost_whenIdIsValid() {
        Optional<Post> foundPost = postDao.findById(1L);

        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getId()).isEqualTo(1L);
        assertThat(foundPost.get().getTitle()).isEqualTo("1 post");
    }

    @Test
    @DisplayName("post dao: find all by username")
    void find_findsAllPostsByUsername_whenDataIsValid() {
        String username = "student";
        PostFilter filter = new PostFilter();
        filter.setUsername(username);

        Page<Post> filteredPostResult2 = postDao.findAll(filter, pageable);

        assertThat(filteredPostResult2.getContent()).isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("post dao: find all by tag")
    @Sql("/db/insert-test-data-into-post_tag-table.sql")
    void find_findsAllPostsByTag_whenDataIsValid() {
        PostFilter filter = new PostFilter();

        filter.setTagId(4L);
        Page<Post> filteredPostResult1 = postDao.findAll(filter, pageable);

        filter.setTagId(3L);
        Page<Post> filteredPostResult2 = postDao.findAll(filter, pageable);

        assertThat(filteredPostResult1.getContent()).isNotEmpty().hasSize(3);
        assertThat(filteredPostResult2.getContent()).isNotEmpty().hasSize(1);
    }
}
