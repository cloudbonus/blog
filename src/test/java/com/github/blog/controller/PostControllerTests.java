package com.github.blog.controller;

import com.github.blog.config.ContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
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
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_info-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-order-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class PostControllerTests {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @Rollback
    @WithUserDetails("student")
    @DisplayName("post controller: create")
    void create_returnsPostDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title": "title_template",
                                "content": "content_template"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("content_template"))
                .andExpect(jsonPath("$.userId").isNotEmpty());
    }

    @Test
    @WithUserDetails("kvossing0")
    @DisplayName("post controller: create - bad request exception")
    void create_throwsExceptionForbidden_whenUserDoesntHaveRightRole() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title": "title_template",
                                "content": "content_template"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Rollback
    @WithUserDetails("student")
    @DisplayName("post controller: update")
    void update_returnsUpdatedPostDto_whenDataIsValid() throws Exception {
        mockMvc.perform(put("/posts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title": "updated_title_template",
                                "content": "updated_content_template"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("updated_content_template"))
                .andExpect(jsonPath("$.title").value("updated_title_template"));
    }

    @Test
    @WithUserDetails("student")
    @DisplayName("post controller: update - bad request exception")
    void update_throwsExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(put("/posts/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title": "updated_title_template",
                                "content": "updated_content_template"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Rollback
    @WithUserDetails("student")
    @DisplayName("post controller: delete")
    void delete_deletesPost_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/posts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("1 post"));
    }

    @Test
    @WithUserDetails("student")
    @DisplayName("post controller: delete - bad request exception")
    void delete_throwsExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(delete("/posts/{id}", 4))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("company")
    @DisplayName("post controller: find by id with completed order")
    void find_findsPostById_whenDataIsValidAndOrderCompleted() throws Exception {
        mockMvc.perform(get("/posts/{id}", 5))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("company")
    @DisplayName("post controller: find by id with new order")
    void find_findsPostById_whenDataIsValidAndOrderNew() throws Exception {
        mockMvc.perform(get("/posts/{id}", 6))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails
    @DisplayName("post controller: find by id - bad request exception")
    void find_throwsExceptionForbidden_whenDataIsValidAndOrderNew() throws Exception {
        mockMvc.perform(get("/posts/{id}", 6))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails
    @DisplayName("post controller: find all")
    void find_findsAllPosts_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(4));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("post controller: find all - forbidden exception")
    void find_throwExceptionUnauthorized_whenUserUnauthorized() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails
    @DisplayName("post controller: find all by username")
    void find_findsAllPostsByUsername_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/posts").param("username", "student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].title").value("1 post"))
                .andExpect(jsonPath("$.content[1].title").value("2 post"));
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("post controller: find by tag")
    @Sql("/db/insert-test-data-into-post_tag-table.sql")
    void find_findsAllPostsByTag_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/posts").param("tagId", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].title").value("1 post"))
                .andExpect(jsonPath("$.content[0].tagIds.size()").value(1))
                .andExpect(jsonPath("$.content[1].title").value("2 post"))
                .andExpect(jsonPath("$.content[2].title").value("3 post"));
    }
}
