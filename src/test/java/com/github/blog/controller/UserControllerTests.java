package com.github.blog.controller;

import com.github.blog.config.ControllerTestConfig;
import com.github.blog.config.WebTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Raman Haurylau
 */
@Transactional
@SpringJUnitWebConfig(classes = {ControllerTestConfig.class, WebTestConfig.class,})
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_info-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class UserControllerTests {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @Rollback
    @WithUserDetails("kvossing0")
    @DisplayName("user controller: update")
    void update_returnsUpdatedUserDto_whenDataIsValid() throws Exception {
        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "email": "updated_email_template@mail.ru"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("updated_email_template@mail.ru"))
                .andExpect(jsonPath("$.username").value("kvossing0"));
    }

    @Test
    @Rollback
    @WithUserDetails("kvossing0")
    @DisplayName("user controller: delete")
    void delete_deletesUser_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("kvossing0"));
    }


    @Test
    @WithUserDetails("gmaccook1")
    @DisplayName("user controller: delete - bad request exception")
    void delete_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails
    @DisplayName("user controller: find all by role")
    void find_findsAllUsersByRole_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/users").param("role", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(6))
                .andExpect(jsonPath("$.content[0].username").value("kvossing0"))
                .andExpect(jsonPath("$.content[1].username").value("gmaccook1"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("user controller: find all - forbidden exception")
    void find_throwExceptionUnauthorized_whenUserUnauthorized() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails
    @DisplayName("user controller: find by username")
    void find_findsUserByUsername_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/users").param("username", "kvossing0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].username").value("kvossing0"));
    }

    @Test
    @WithUserDetails
    @DisplayName("user controller: find all by job")
    void find_findsAllUsersByJob_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/users").param("job", "ai researcher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].username").value("gmaccook1"));
    }

    @Test
    @WithUserDetails
    @DisplayName("user controller: find all by job - not found exception")
    void find_throwsExceptionNotFound_whenDataIsInvalid() throws Exception {
        mockMvc.perform(get("/users").param("job", "test work"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails
    @DisplayName("user controller: find all by university")
    void find_findsAllUsersByUniversity_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/users").param("university", "mit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content.[0].username").value("gmaccook1"));
    }
}
