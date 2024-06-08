package com.github.blog.controller;


import com.github.blog.config.ControllerTestConfig;
import com.github.blog.config.WebTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
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
@SpringJUnitWebConfig(classes = {ControllerTestConfig.class, WebTestConfig.class,})
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_info-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-comment-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class CommentControllerTests {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @Rollback
    @WithUserDetails
    @DisplayName("comment controller: create")
    void create_returnsCommentDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "postId": 1,
                                "content": "content_template"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.content").value("content_template"));
    }

    @Test
    @Rollback
    @WithUserDetails("kvossing0")
    @DisplayName("comment controller: update")
    void update_returnsUpdatedCommentDto_whenDataIsValid() throws Exception {
        mockMvc.perform(put("/comments/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "content": "updated_content_template"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("updated_content_template"));
    }

    @Test
    @WithUserDetails
    @DisplayName("comment controller: update - bad request exception")
    void update_throwsExceptionBadRequest_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(put("/comments/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "content": "updated_content_template"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Rollback
    @WithUserDetails("kvossing0")
    @DisplayName("comment controller: delete")
    void delete_deletesComment_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/comments/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("1 content"));
    }

    @Test
    @WithUserDetails("gmaccook1")
    @DisplayName("comment controller: delete - bad request exception")
    void delete_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails
    @DisplayName("comment controller: find by id")
    void find_findsById_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/comments/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("1 content"));
    }

    @Test
    @WithUserDetails
    @DisplayName("comment controller: find all by username")
    void find_findsAllCommentsByUsername_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/comments").param("username", "kvossing0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.content[0].content").value("1 content"))
                .andExpect(jsonPath("$.content[1].content").value("2 content"));
    }
}
