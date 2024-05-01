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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Raman Haurylau
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringJUnitWebConfig(WebTestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CommentControllerTests {
    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @DisplayName("comment controller: create")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void create_returnsCommentDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "postId": 1,
                                "userId": 1,
                                "content": "Hello World!",
                                "publishedAt": 1714320006.512803000
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.content").value("Hello World!"))
                .andExpect(jsonPath("$.postId").value(1));
    }

    @Test
    @DisplayName("comment controller: update")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void update_returnsUpdatedCommentDto_whenDataIsValid() throws Exception {
        mockMvc.perform(put("/comments/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "content": "updated content"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("updated content"));
    }

    @Test
    @DisplayName("comment controller: update exception")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void create_throwsExceptionBadRequest_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "invalid_data": "Hello there!!"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("comment controller: delete")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void delete_deletesComment_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/comments/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("This is the content of the first comment."));
    }

    @Test
    @DisplayName("comment controller: findById")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void find_findsById_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/comments/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("This is the content of the first comment."));
    }

    @Test
    @DisplayName("comment controller: allByLogin")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void find_findsAllCommentsByLogin_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/comments").param("login", "kvossing0"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.content[0].content").value("This is the content of the first comment."))
                .andExpect(jsonPath("$.content[1].content").value("This is the content of the second comment."));
    }
}
