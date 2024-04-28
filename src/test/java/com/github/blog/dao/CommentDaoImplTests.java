package com.github.blog.dao;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.model.Comment;
import com.github.blog.model.Post;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raman Haurylau
 */
@Transactional
@ExtendWith({SpringExtension.class})
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
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-comment-table.sql"})
    void create_returnsCommentDto_whenDataIsValid() {
        String login = "kvossing0";

        Optional<User> u = userDao.findByLogin(login);
        assertThat(u).isPresent();

        User user = u.get();
        assertThat(user.getLogin()).isEqualTo(login);

        List<Comment> allComments = commentDao.findAll();

        assertThat(allComments).isNotEmpty().hasSize(3);

        List<Post> allPosts = postDao.findAll();

        assertThat(allPosts).isNotEmpty().hasSize(3);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContent("Hello World!");
        comment.setPublishedAt(OffsetDateTime.now());
        comment.setPost(allPosts.get(0));

        comment = commentDao.create(comment);

        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isNotNull();

        allComments = commentDao.findAll();

        assertThat(allComments).isNotEmpty().hasSize(4);
    }

    @Test
    @DisplayName("comment dao: update")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-comment-table.sql"})
    void update_returnsUpdatedCommentDto_whenDataIsValid() {
        String login = "kvossing0";

        List<Comment> allComments = commentDao.findAllByLogin(login);

        assertThat(allComments).isNotEmpty().hasSize(2);

        Comment comment = allComments.get(0);

        Long updatedId = comment.getId();
        String updatedContent = "Updated content";

        comment.setContent(updatedContent);

        commentDao.update(comment);

        Optional<Comment> c = commentDao.findById(updatedId);
        assertThat(c).isPresent();

        comment = c.get();
        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isEqualTo(updatedId);
        assertThat(comment.getContent()).isEqualTo(updatedContent);
    }

    @Test
    @DisplayName("comment dao: delete")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-comment-table.sql"})
    void delete_deletesComment_whenDataIsValid() {
        String login = "kvossing0";

        List<Comment> allCommentsByLogin = commentDao.findAllByLogin(login);

        assertThat(allCommentsByLogin).isNotEmpty().hasSize(2);

        Comment comment = allCommentsByLogin.get(0);

        commentDao.delete(comment);

        allCommentsByLogin = commentDao.findAllByLogin(login);

        assertThat(allCommentsByLogin).isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("comment dao: allByLogin")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-comment-table.sql"})
    void find_findsAllCommentsByLogin_whenDataIsValid() {
        String login1 = "kvossing0";
        String login2 = "gmaccook1";

        List<Comment> allCommentsByLogin1 = commentDao.findAllByLogin(login1);

        assertThat(allCommentsByLogin1).isNotEmpty().hasSize(2);

        List<Comment> allCommentsByLogin2 = commentDao.findAllByLogin(login2);

        assertThat(allCommentsByLogin2).isNotEmpty().hasSize(1);
    }
}
