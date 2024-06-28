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
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class TagControllerIntegrationTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @Rollback
    @WithUserDetails("admin")
    @DisplayName("tag controller: create")
    void create_returnsCreatedTagDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "TEST"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithUserDetails
    @DisplayName("tag controller: create - bad request exception")
    void create_throwsExceptionForbidden_whenUserDoesntHaveRightRole() throws Exception {
        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "TEST"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Rollback
    @WithUserDetails("admin")
    @DisplayName("tag controller: update")
    void update_returnsUpdatedTagDto_whenDataIsValid() throws Exception {
        mockMvc.perform(put("/tags/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "TEST"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Rollback
    @WithUserDetails("admin")
    @DisplayName("tag controller: delete")
    void delete_returnsDeletedTagDto_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/tags/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("tag controller: find by id")
    void find_findsTagById_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/tags/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("tag controller: find all")
    @Sql({"/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-post_tag-table.sql"})
    void find_findsAllTagsByPostId_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/tags").param("postId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }
}
