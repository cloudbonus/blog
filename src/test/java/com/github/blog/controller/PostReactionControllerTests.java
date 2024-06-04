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
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-post_reaction-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class PostReactionControllerTests {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @Rollback
    @WithUserDetails
    @DisplayName("post reaction controller: create")
    void create_returnsCreatedPostReactionDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/post-reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "postId": 2,
                                "reactionId": 2
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithUserDetails
    @DisplayName("post reaction controller: create - validation exception")
    void create_throwsExceptionForbidden_whenReactionAlreadyExists() throws Exception {
        mockMvc.perform(post("/post-reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "postId": 1,
                                "reactionId": 1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Rollback
    @WithUserDetails
    @DisplayName("post reaction controller: update")
    void update_returnsUpdatedPostReactionDto_whenDataIsValid() throws Exception {
        mockMvc.perform(put("/post-reactions/{id}", 1)
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
    @WithUserDetails
    @DisplayName("post reaction controller: delete")
    void delete_returnsDeletedPostReactionDto_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/post-reactions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithUserDetails
    @DisplayName("post reaction controller: find by id")
    void find_findsPostReactionById_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/post-reactions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithUserDetails
    @DisplayName("post reaction controller: find all")
    void find_findsAllPostReactions_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/post-reactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }
}
