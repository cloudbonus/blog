package com.github.blog.integration;

import com.github.blog.config.ContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Raman Haurylau
 */
@Transactional
@SpringBootTest(classes = ContainerConfig.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-comment-table.sql", "/db/insert-test-data-into-comment_reaction-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class CommentReactionControllerIntegrationTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @Rollback
    @WithUserDetails
    @DisplayName("comment reaction controller: create")
    void create_returnsCreatedCommentReactionDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/comment-reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "commentId": 2,
                                "reactionId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithUserDetails("kvossing0")
    @DisplayName("comment reaction controller: create - validation exception")
    void create_throwsExceptionForbidden_whenReactionAlreadyExists() throws Exception {
        mockMvc.perform(post("/comment-reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "commentId": 1,
                                "reactionId": 1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Rollback
    @WithUserDetails("kvossing0")
    @DisplayName("comment reaction controller: update")
    void update_returnsUpdatedCommentReactionDto_whenDataIsValid() throws Exception {
        mockMvc.perform(put("/comment-reactions/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "reactionId": 2
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reactionId").value(2));
    }

    @Test
    @Rollback
    @WithUserDetails("kvossing0")
    @DisplayName("comment reaction controller: delete")
    void delete_returnsDeletedCommentReactionDto_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/comment-reactions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithUserDetails
    @DisplayName("comment reaction controller: find by id")
    void find_findsCommentReactionById_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/comment-reactions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithUserDetails
    @DisplayName("comment reaction controller: find all")
    void find_findsAllCommentReactions_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/comment-reactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }
}
