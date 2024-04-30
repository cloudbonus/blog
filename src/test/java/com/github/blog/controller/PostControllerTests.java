package com.github.blog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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
    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @DisplayName("post controller: create")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql"})
    void create_returnsPostDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "userId": 1,
                                "title": "test title",
                                "content": "test content",
                                "publishedAt": 1714320006.512803000
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.content").value("test content"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @DisplayName("post controller: update")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-post_tag-table.sql"})
    void update_returnsUpdatedPostDto_whenDataIsValid() throws Exception {
        mockMvc.perform(put("/posts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title": "updated title",
                                "content": "updated content"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("updated content"))
                .andExpect(jsonPath("$.title").value("updated title"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @DisplayName("post controller: delete")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-post_tag-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void delete_deletesPost_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/posts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("First Post"));
    }

    @Test
    @DisplayName("post controller: findAllByLogin")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql"})
    void find_findsAllPostsByLogin_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/posts").param("login", "kvossing0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("First Post"))
                .andExpect(jsonPath("$[1].title").value("Second Post"));
    }

    @Test
    @DisplayName("post controller: findByTag")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-post_tag-table.sql"})
    void find_findsAllPostsByTag_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/posts").param("tag", "news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].title").value("First Post"))
                .andExpect(jsonPath("$[0].tagIds.size()").value(1))
                .andExpect(jsonPath("$[1].title").value("Second Post"))
                .andExpect(jsonPath("$[2].title").value("Third Post"));
    }
}
