package com.github.blog.dao;

import com.github.blog.config.DataSourceProperties;
import com.github.blog.config.PersistenceJPAConfig;
import com.github.blog.dto.filter.CommentDtoFilter;
import com.github.blog.dto.filter.PostDtoFilter;
import com.github.blog.dto.filter.UserDtoFilter;
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
    @Sql({"/db/daotests/insert-test-data-into-user-table.sql", "/db/daotests/insert-test-data-into-post-table.sql", "/db/daotests/insert-test-data-into-comment-table.sql"})
    void create_returnsCommentDto_whenDataIsValid() {
        String login = "kvossing0";

        UserDtoFilter userFilter = new UserDtoFilter();
        userFilter.setLogin(login);

        List<User> filteredUserResult = userDao.findAll(userFilter);

        assertThat(filteredUserResult).isNotEmpty();

        PostDtoFilter postFilter = new PostDtoFilter();
        postFilter.setLogin(login);

        List<Post> filteredPostResult = postDao.findAll(postFilter);
        assertThat(filteredPostResult).isNotEmpty();

        Comment comment = new Comment();
        comment.setUser(filteredUserResult.get(0));
        comment.setContent("Hello World!");
        comment.setPublishedAt(OffsetDateTime.now());
        comment.setPost(filteredPostResult.get(0));

        comment = commentDao.create(comment);

        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isNotNull();

        assertThat(commentDao.findAll()).isNotEmpty().hasSize(4);
    }

    @Test
    @DisplayName("comment dao: update")
    @Sql({"/db/daotests/insert-test-data-into-user-table.sql", "/db/daotests/insert-test-data-into-post-table.sql", "/db/daotests/insert-test-data-into-comment-table.sql"})
    void update_returnsUpdatedCommentDto_whenDataIsValid() {
        String login = "kvossing0";
        CommentDtoFilter filter = new CommentDtoFilter();
        filter.setLogin(login);

        List<Comment> filteredCommentResult = commentDao.findAll(filter);

        assertThat(filteredCommentResult).isNotEmpty();

        Comment comment = filteredCommentResult.get(0);

        Long updatedId = comment.getId();
        String updatedContent = "Updated content";
        comment.setContent(updatedContent);

        commentDao.update(comment);

        Optional<Comment> c = commentDao.findById(updatedId);
        assertThat(c).isPresent();

        assertThat(c.get()).isNotNull();
        assertThat(c.get().getId()).isEqualTo(updatedId);
        assertThat(c.get().getContent()).isEqualTo(updatedContent);
    }

    @Test
    @DisplayName("comment dao: delete")
    @Sql({"/db/daotests/insert-test-data-into-user-table.sql", "/db/daotests/insert-test-data-into-post-table.sql", "/db/daotests/insert-test-data-into-comment-table.sql"})
    void delete_deletesComment_whenDataIsValid() {
        String login = "kvossing0";
        CommentDtoFilter filter = new CommentDtoFilter();
        filter.setLogin(login);

        List<Comment> filteredCommentResult = commentDao.findAll(filter);

        assertThat(filteredCommentResult).isNotEmpty();

        commentDao.delete(filteredCommentResult.get(0));

        assertThat(commentDao.findAll()).isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("comment dao: allByLogin")
    @Sql({"/db/daotests/insert-test-data-into-user-table.sql", "/db/daotests/insert-test-data-into-post-table.sql", "/db/daotests/insert-test-data-into-comment-table.sql"})
    void find_findsAllCommentsByLogin_whenDataIsValid() {
        String login1 = "kvossing0";
        String login2 = "gmaccook1";

        CommentDtoFilter filter = new CommentDtoFilter();
        filter.setLogin(login1);

        List<Comment> filteredCommentResult1 = commentDao.findAll(filter);

        filter.setLogin(login2);
        List<Comment> filteredCommentResult2 = commentDao.findAll(filter);

        assertThat(filteredCommentResult1).isNotEmpty().hasSize(2);
        assertThat(filteredCommentResult2).isNotEmpty().hasSize(1);
    }
}
