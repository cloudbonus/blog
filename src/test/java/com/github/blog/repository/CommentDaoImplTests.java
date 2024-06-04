package com.github.blog.repository;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.config.RepositoryTestConfig;
import com.github.blog.config.WebTestConfig;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
import com.github.blog.model.User;
import com.github.blog.repository.dto.common.Page;
import com.github.blog.repository.dto.common.Pageable;
import com.github.blog.repository.dto.filter.CommentFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringJUnitConfig(classes = {WebTestConfig.class, RepositoryTestConfig.class, PersistenceJPAConfig.class, DataSourceProperties.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-comment-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class CommentDaoImplTests {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Test
    @Rollback
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
        comment.setCreatedAt(OffsetDateTime.now());

        comment = commentDao.create(comment);

        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isNotNull();

        assertThat(commentDao.findAll()).isNotEmpty().hasSize(4);
    }

    @Test
    @Rollback
    @DisplayName("comment dao: update")
    void update_returnsUpdatedCommentDto_whenDataIsValid() {
        Optional<Comment> c = commentDao.findById(3L);

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
    @Rollback
    @DisplayName("comment dao: delete")
    void delete_deletesComment_whenDataIsValid() {
        Optional<Comment> comment = commentDao.findById(1L);

        assertThat(comment).isPresent();

        commentDao.delete(comment.get());

        assertThat(commentDao.findAll()).isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("comment dao: find by id")
    void findById_returnsComment_whenIdIsValid() {
        Optional<Comment> foundComment = commentDao.findById(1L);

        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("comment dao: all by username")
    void find_findsAllCommentsByUsername_whenDataIsValid() {
        String username1 = "kvossing0";
        String username2 = "gmaccook1";

        CommentFilter filter = new CommentFilter();
        filter.setUsername(username1);

        Pageable pageable = new Pageable();
        pageable.setPageSize(Integer.MAX_VALUE);
        pageable.setPageNumber(1);
        pageable.setOrderBy("ASC");

        Page<Comment> filteredCommentResult1 = commentDao.findAll(filter, pageable);

        filter.setUsername(username2);
        Page<Comment> filteredCommentResult2 = commentDao.findAll(filter, pageable);

        assertThat(filteredCommentResult1.getContent()).isNotEmpty().hasSize(2);
        assertThat(filteredCommentResult2.getContent()).isNotEmpty().hasSize(1);
    }
}
