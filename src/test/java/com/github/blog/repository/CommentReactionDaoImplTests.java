package com.github.blog.repository;

import com.github.blog.config.ContainerConfig;
import com.github.blog.model.Comment;
import com.github.blog.model.CommentReaction;
import com.github.blog.model.Reaction;
import com.github.blog.model.User;
import com.github.blog.repository.filter.CommentReactionFilter;
import com.github.blog.repository.specification.CommentReactionSpecification;
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
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-comment-table.sql", "/db/insert-test-data-into-comment_reaction-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class CommentReactionDaoImplTests {

    @Autowired
    private CommentReactionRepository commentReactionRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private UserRepository userDao;

    private static Pageable pageable;

    @BeforeAll
    public static void setUp() {
        pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id").ascending());
    }

    @Test
    @Rollback
    @DisplayName("comment reaction dao: create")
    void create_returnsCommentReaction_whenDataIsValid() {
        Optional<Comment> optionalComment = commentRepository.findById(1L);
        assertThat(optionalComment).isPresent();
        Comment comment = optionalComment.get();

        Optional<Reaction> optionalReaction = reactionRepository.findById(2L);
        assertThat(optionalReaction).isPresent();
        Reaction reaction = optionalReaction.get();

        Optional<User> optionalUser = userDao.findById(1L);
        assertThat(optionalUser).isPresent();
        User user = optionalUser.get();

        CommentReaction newCommentReaction = new CommentReaction();
        newCommentReaction.setComment(comment);
        newCommentReaction.setReaction(reaction);
        newCommentReaction.setUser(user);

        CommentReaction createdCommentReaction = commentReactionRepository.save(newCommentReaction);

        assertThat(createdCommentReaction).isNotNull();
        assertThat(createdCommentReaction.getId()).isNotNull();
        assertThat(createdCommentReaction.getComment().getId()).isEqualTo(comment.getId());
        assertThat(createdCommentReaction.getReaction().getId()).isEqualTo(reaction.getId());
        assertThat(createdCommentReaction.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("comment reaction dao: find by id")
    void findById_returnsCommentReaction_whenIdIsValid() {
        Optional<CommentReaction> foundCommentReaction = commentReactionRepository.findById(1L);

        assertThat(foundCommentReaction).isPresent();
        assertThat(foundCommentReaction.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("comment reaction dao: update")
    @Rollback
    void update_returnsUpdatedCommentReaction_whenDataIsValid() {
        Optional<CommentReaction> optionalCommentReaction = commentReactionRepository.findById(1L);
        assertThat(optionalCommentReaction).isPresent();
        CommentReaction updatedCommentReaction = optionalCommentReaction.get();

        Optional<Reaction> optionalReaction = reactionRepository.findById(2L);
        assertThat(optionalReaction).isPresent();
        Reaction updatedReaction = optionalReaction.get();

        updatedCommentReaction.setReaction(updatedReaction);

        updatedCommentReaction = commentReactionRepository.save(updatedCommentReaction);

        assertThat(updatedCommentReaction).isNotNull();
        assertThat(updatedCommentReaction.getReaction().getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("comment reaction dao: delete")
    @Rollback
    void delete_deletesCommentReaction_whenIdIsValid() {
        Optional<CommentReaction> optionalCommentReaction = commentReactionRepository.findById(1L);

        assertThat(optionalCommentReaction).isPresent();

        CommentReaction deletedCommentReaction = optionalCommentReaction.get();

        commentReactionRepository.delete(deletedCommentReaction);

        assertThat(commentReactionRepository.findById(1L)).isNotPresent();
    }

    @Test
    @DisplayName("comment reaction dao: find all with filter and pagination")
    void findAll_withFilterAndPagination_returnsFilteredAndPagedCommentReactions() {
        CommentReactionFilter filter = new CommentReactionFilter();
        filter.setUsername("student");

        Page<CommentReaction> commentReactionsPage = commentReactionRepository
                .findAll(CommentReactionSpecification.filterBy(filter), pageable);

        assertThat(commentReactionsPage.getContent()).isNotEmpty();
    }
}
