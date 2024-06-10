package com.github.blog.repository;

import com.github.blog.config.ContainerConfig;
import com.github.blog.model.Post;
import com.github.blog.model.PostReaction;
import com.github.blog.model.Reaction;
import com.github.blog.model.User;
import com.github.blog.repository.filter.PostReactionFilter;
import com.github.blog.repository.specification.PostReactionSpecification;
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
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-post_reaction-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class PostReactionDaoImplTests {

    @Autowired
    private PostReactionRepository postReactionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private UserRepository userRepository;

    private static Pageable pageable;

    @BeforeAll
    public static void setUp() {
        pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id").ascending());
    }

    @Test
    @Rollback
    @DisplayName("post reaction dao: create")
    void create_returnsPostReaction_whenDataIsValid() {
        Optional<Post> optionalPost = postRepository.findById(1L);
        assertThat(optionalPost).isPresent();
        Post post = optionalPost.get();

        Optional<Reaction> optionalReaction = reactionRepository.findById(1L);
        assertThat(optionalReaction).isPresent();
        Reaction reaction = optionalReaction.get();

        Optional<User> optionalUser = userRepository.findById(1L);
        assertThat(optionalUser).isPresent();
        User user = optionalUser.get();

        PostReaction newPostReaction = new PostReaction();
        newPostReaction.setPost(post);
        newPostReaction.setReaction(reaction);
        newPostReaction.setUser(user);

        PostReaction createdPostReaction = postReactionRepository.save(newPostReaction);

        assertThat(createdPostReaction).isNotNull();
        assertThat(createdPostReaction.getId()).isNotNull();
        assertThat(createdPostReaction.getPost().getId()).isEqualTo(post.getId());
        assertThat(createdPostReaction.getReaction().getId()).isEqualTo(reaction.getId());
        assertThat(createdPostReaction.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("post reaction dao: find by id")
    void findById_returnsPostReaction_whenIdIsValid() {
        Optional<PostReaction> foundPostReaction = postReactionRepository.findById(1L);

        assertThat(foundPostReaction).isPresent();
        assertThat(foundPostReaction.get().getId()).isEqualTo(1L);
    }

    @Test
    @Rollback
    @DisplayName("post reaction dao: update")
    void update_returnsUpdatedPostReaction_whenDataIsValid() {
        Optional<PostReaction> optionalPostReaction = postReactionRepository.findById(1L);
        assertThat(optionalPostReaction).isPresent();
        PostReaction updatedPostReaction = optionalPostReaction.get();

        Optional<Reaction> optionalReaction = reactionRepository.findById(2L);
        assertThat(optionalReaction).isPresent();
        Reaction updatedReaction = optionalReaction.get();

        updatedPostReaction.setReaction(updatedReaction);

        updatedPostReaction = postReactionRepository.save(updatedPostReaction);

        assertThat(updatedPostReaction).isNotNull();
        assertThat(updatedPostReaction.getReaction().getId()).isEqualTo(2L);
    }

    @Test
    @Rollback
    @DisplayName("post reaction dao: delete")
    void delete_deletesPostReaction_whenIdIsValid() {
        Optional<PostReaction> optionalPostReaction = postReactionRepository.findById(1L);

        assertThat(optionalPostReaction).isPresent();

        PostReaction deletedPostReaction = optionalPostReaction.get();

        postReactionRepository.delete(deletedPostReaction);

        assertThat(postReactionRepository.findById(1L)).isNotPresent();
    }

    @Test
    @DisplayName("post reaction dao: find all with filter and pagination")
    void findAll_withFilterAndPagination_returnsFilteredAndPagedPostReactions() {
        PostReactionFilter filter = new PostReactionFilter();
        filter.setUsername("student");

        Page<PostReaction> postReactionsPage = postReactionRepository
                .findAll(PostReactionSpecification.filterBy(filter), pageable);

        assertThat(postReactionsPage.getContent()).isNotEmpty();
    }
}
