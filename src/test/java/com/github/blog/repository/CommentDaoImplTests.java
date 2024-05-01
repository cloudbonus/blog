package com.github.blog.repository;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.controller.dto.response.Page;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.dto.filter.CommentFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/daotests/insert-test-data-into-user-table.sql", "/db/daotests/insert-test-data-into-post-table.sql", "/db/daotests/insert-test-data-into-comment-table.sql"})
@Transactional
@ExtendWith({SpringExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {DaoTestConfig.class, PersistenceJPAConfig.class, DataSourceProperties.class})
public class CommentDaoImplTests {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Test
    @DisplayName("comment dao: create")
    void create_returnsCommentDto_whenDataIsValid() {
        Optional<User> user = userDao.findById(1L);
        Optional<Post> post = postDao.findById(1L);

        assertThat(user).isPresent();
        assertThat(post).isPresent();

        Comment comment = new Comment();
        comment.setUser(user.get());
        comment.setPost(post.get());
        comment.setContent("Hello World!");
        comment.setPublishedAt(OffsetDateTime.now());

        comment = commentDao.create(comment);

        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isNotNull();

        assertThat(commentDao.findAll()).isNotEmpty().hasSize(4);
    }

    @Test
    @DisplayName("comment dao: update")
    void update_returnsUpdatedCommentDto_whenDataIsValid() {
        Optional<Comment> c = commentDao.findById(1L);

        assertThat(c).isPresent();

        Comment comment = c.get();

        Long updatedId = comment.getId();
        String updatedContent = "Updated content";
        comment.setContent(updatedContent);

        commentDao.update(comment);

        c = commentDao.findById(updatedId);

        assertThat(c).isPresent();
        assertThat(c.get()).isNotNull();
        assertThat(c.get().getId()).isEqualTo(updatedId);
        assertThat(c.get().getContent()).isEqualTo(updatedContent);
    }

    @Test
    @DisplayName("comment dao: delete")
    void delete_deletesComment_whenDataIsValid() {
        Optional<Comment> comment = commentDao.findById(1L);

        assertThat(comment).isPresent();

        commentDao.delete(comment.get());

        assertThat(commentDao.findAll()).isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("comment dao: allByLogin")
    void find_findsAllCommentsByLogin_whenDataIsValid() {
        String login1 = "kvossing0";
        String login2 = "gmaccook1";

        CommentFilter filter = new CommentFilter();
        filter.setLogin(login1);

        Page<Comment> filteredCommentResult1 = commentDao.findAll(filter);

        filter.setLogin(login2);
        Page<Comment> filteredCommentResult2 = commentDao.findAll(filter);

        assertThat(filteredCommentResult1.getContent()).isNotEmpty().hasSize(2);
        assertThat(filteredCommentResult2.getContent()).isNotEmpty().hasSize(1);
    }
}
