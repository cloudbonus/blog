package com.github.blog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringJUnitWebConfig(WebTestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PostControllerTests {
    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("post controller: create")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql"})
    void create_returnsPostDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "userId": 1,
                                "title": "title_template",
                                "content": "content_template"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.content").value("content_template"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @DisplayName("post controller: create exception")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql"})
    void create_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "userId": 2,
                                "title": "title_template",
                                "content": "content_template"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("post controller: update")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-post_tag-table.sql"})
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
                .andExpect(jsonPath("$.title").value("updated_title_template"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @DisplayName("post controller: delete")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-post_tag-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void delete_deletesPost_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/posts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("First Post"));
    }

    @Test
    @DisplayName("post controller: delete exception")
    @WithUserDetails("gmaccook1")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-post_tag-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void delete_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(delete("/posts/{id}", 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("post controller: findAll exception")
    @WithAnonymousUser
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql"})
    void find_throwExceptionUnauthorized_whenUserUnauthorized() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("post controller: findAllByLogin")
    @WithMockUser
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql"})
    void find_findsAllPostsByLogin_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/posts").param("login", "kvossing0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.content[0].title").value("First Post"))
                .andExpect(jsonPath("$.content[1].title").value("Second Post"));
    }

    @Test
    @DisplayName("post controller: findByTag")
    @WithMockUser
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-post_tag-table.sql"})
    void find_findsAllPostsByTag_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/posts").param("tag", "news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.content[0].title").value("First Post"))
                .andExpect(jsonPath("$.content[0].tagIds.size()").value(1))
                .andExpect(jsonPath("$.content[1].title").value("Second Post"))
                .andExpect(jsonPath("$.content[2].title").value("Third Post"));
    }
}
