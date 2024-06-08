package com.github.blog.repository;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.config.RepositoryTestConfig;
import com.github.blog.config.WebTestConfig;
import com.github.blog.model.Comment;
import com.github.blog.model.CommentReaction;
import com.github.blog.model.Reaction;
import com.github.blog.model.User;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.CommentReactionFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringJUnitConfig(classes = {WebTestConfig.class, RepositoryTestConfig.class, PersistenceJPAConfig.class, DataSourceProperties.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-comment-table.sql", "/db/insert-test-data-into-comment_reaction-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class CommentReactionDaoImplTests {

    @Autowired
    private CommentReactionDao commentReactionDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ReactionDao reactionDao;

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
    @DisplayName("comment reaction dao: create")
    @Rollback
    void create_returnsCommentReaction_whenDataIsValid() {
        Optional<Comment> optionalComment = commentDao.findById(1L);
        assertThat(optionalComment).isPresent();
        Comment comment = optionalComment.get();

        Optional<Reaction> optionalReaction = reactionDao.findById(2L);
        assertThat(optionalReaction).isPresent();
        Reaction reaction = optionalReaction.get();

        Optional<User> optionalUser = userDao.findById(1L);
        assertThat(optionalUser).isPresent();
        User user = optionalUser.get();

        CommentReaction newCommentReaction = new CommentReaction();
        newCommentReaction.setComment(comment);
        newCommentReaction.setReaction(reaction);
        newCommentReaction.setUser(user);

        CommentReaction createdCommentReaction = commentReactionDao.create(newCommentReaction);

        assertThat(createdCommentReaction).isNotNull();
        assertThat(createdCommentReaction.getId()).isNotNull();
        assertThat(createdCommentReaction.getComment().getId()).isEqualTo(comment.getId());
        assertThat(createdCommentReaction.getReaction().getId()).isEqualTo(reaction.getId());
        assertThat(createdCommentReaction.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("comment reaction dao: find by id")
    void findById_returnsCommentReaction_whenIdIsValid() {
        Optional<CommentReaction> foundCommentReaction = commentReactionDao.findById(1L);

        assertThat(foundCommentReaction).isPresent();
        assertThat(foundCommentReaction.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("comment reaction dao: update")
    @Rollback
    void update_returnsUpdatedCommentReaction_whenDataIsValid() {
        Optional<CommentReaction> optionalCommentReaction = commentReactionDao.findById(1L);
        assertThat(optionalCommentReaction).isPresent();
        CommentReaction updatedCommentReaction = optionalCommentReaction.get();

        Optional<Reaction> optionalReaction = reactionDao.findById(2L);
        assertThat(optionalReaction).isPresent();
        Reaction updatedReaction = optionalReaction.get();

        updatedCommentReaction.setReaction(updatedReaction);

        updatedCommentReaction = commentReactionDao.update(updatedCommentReaction);

        assertThat(updatedCommentReaction).isNotNull();
        assertThat(updatedCommentReaction.getReaction().getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("comment reaction dao: delete")
    @Rollback
    void delete_deletesCommentReaction_whenIdIsValid() {
        Optional<CommentReaction> optionalCommentReaction = commentReactionDao.findById(1L);

        assertThat(optionalCommentReaction).isPresent();

        CommentReaction deletedCommentReaction = optionalCommentReaction.get();

        commentReactionDao.delete(deletedCommentReaction);

        assertThat(commentReactionDao.findById(1L)).isNotPresent();
    }

    @Test
    @DisplayName("comment reaction dao: find all with filter and pagination")
    void findAll_withFilterAndPagination_returnsFilteredAndPagedCommentReactions() {
        CommentReactionFilter filter = new CommentReactionFilter();
        filter.setUsername("student");

        Page<CommentReaction> commentReactionsPage = commentReactionDao.findAll(filter, pageable);

        assertThat(commentReactionsPage.getContent()).isNotEmpty();
    }
}
