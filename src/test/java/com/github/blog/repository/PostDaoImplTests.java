package com.github.blog.repository;

import com.github.blog.config.ContainerConfig;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.filter.PostFilter;
import com.github.blog.repository.filter.UserFilter;
import com.github.blog.repository.specification.PostSpecification;
import com.github.blog.repository.specification.UserSpecification;
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

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@SpringBootTest(classes = ContainerConfig.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-post-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
class PostDaoImplTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private static Pageable pageable;

    @BeforeAll
    public static void setUp() {
        pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id").ascending());
    }

    @Test
    @Rollback
    @DisplayName("post dao: create")
    void create_returnsPostDto_whenDataIsValid() {
        String content = "This is the content of the first post.";
        String username = "kvossing0";

        UserFilter filter = new UserFilter();
        filter.setUsername(username);

        Page<User> filteredUserResult = userRepository.findAll(UserSpecification.filterBy(filter), pageable);

        assertThat(filteredUserResult.getContent()).isNotEmpty();

        Post post = new Post();
        post.setUser(filteredUserResult.getContent().get(0));
        post.setTitle("First Post");
        post.setContent(content);
        post.setCreatedAt(OffsetDateTime.now());

        post = postRepository.save(post);

        assertThat(post).isNotNull();
        assertThat(post.getId()).isNotNull();
        assertThat(post.getContent()).isEqualTo(content);

        Iterable<Post> allPosts = postRepository.findAll();

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

        Page<Post> filteredPostResult = postRepository.findAll(PostSpecification.filterBy(filter), pageable);

        assertThat(filteredPostResult.getContent()).isNotEmpty().hasSize(3);

        Post post = filteredPostResult.getContent().get(0);
        String updatedContent = "Look at me i've made it!!!";

        post.setContent(updatedContent);

        post = postRepository.save(post);

        assertThat(post).isNotNull();
        assertThat(post.getContent()).isEqualTo(updatedContent);
        assertThat(post.getTags()).hasSize(1);
    }

    @Test
    @Rollback
    @DisplayName("post dao: delete")
    void delete_deletesPost_whenDataIsValid() {
        Optional<Post> post = postRepository.findById(1L);

        assertThat(post).isPresent();

        postRepository.delete(post.get());

        assertThat(postRepository.findAll()).isNotEmpty().hasSize(6);
    }

    @Test
    @DisplayName("post dao: find by id")
    void findById_returnsPost_whenIdIsValid() {
        Optional<Post> foundPost = postRepository.findById(1L);

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

        Page<Post> filteredPostResult2 = postRepository.findAll(PostSpecification.filterBy(filter), pageable);

        assertThat(filteredPostResult2.getContent()).isNotEmpty().hasSize(3);
    }

    @Test
    @DisplayName("post dao: find all by tag")
    @Sql("/db/insert-test-data-into-post_tag-table.sql")
    void find_findsAllPostsByTag_whenDataIsValid() {
        PostFilter filter = new PostFilter();

        filter.setTagId(4L);
        Page<Post> filteredPostResult1 = postRepository.findAll(PostSpecification.filterBy(filter), pageable);

        filter.setTagId(3L);
        Page<Post> filteredPostResult2 = postRepository.findAll(PostSpecification.filterBy(filter), pageable);

        assertThat(filteredPostResult1.getContent()).isNotEmpty().hasSize(3);
        assertThat(filteredPostResult2.getContent()).isNotEmpty().hasSize(1);
    }
}
