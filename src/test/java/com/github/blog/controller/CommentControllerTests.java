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
    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("comment controller: create")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void create_returnsCommentDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "postId": 1,
                                "userId": 1,
                                "content": "content_template"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.content").value("content_template"))
                .andExpect(jsonPath("$.postId").value(1));
    }

    @Test
    @DisplayName("comment controller: create exception")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void create_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "postId": 1,
                                "userId": 2,
                                "content": "content_template"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("comment controller: update")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
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
    @DisplayName("comment controller: update exception")
    @WithMockUser
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void update_throwsExceptionBadRequest_whenDataIsInvalid() throws Exception {
        mockMvc.perform(put("/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "invalid_data": "bad_template"
                                }
                                """))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("comment controller: delete")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void delete_deletesComment_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/comments/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("This is the content of the first comment."));
    }

    @Test
    @DisplayName("comment controller: delete exception")
    @WithUserDetails("gmaccook1")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void delete_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("comment controller: findById")
    @WithMockUser
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void find_findsById_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/comments/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("This is the content of the first comment."));
    }

    @Test
    @DisplayName("comment controller: findAll exception")
    @WithAnonymousUser
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void find_throwExceptionUnauthorized_whenUserUnauthorized() throws Exception {
        mockMvc.perform(get("/comments").param("login", "kvossing0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("comment controller: findAllByLogin")
    @WithMockUser
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql", "/db/controllertests/insert-test-data-into-post-table.sql", "/db/controllertests/insert-test-data-into-comment-table.sql"})
    void find_findsAllCommentsByLogin_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/comments").param("login", "kvossing0"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.content[0].content").value("This is the content of the first comment."))
                .andExpect(jsonPath("$.content[1].content").value("This is the content of the second comment."));
    }
}
