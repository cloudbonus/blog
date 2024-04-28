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
public class UserControllerTests {
    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @DisplayName("user controller: create")
    @Sql("/db/insert-test-data-into-user-table.sql")
    void create_returnsUserDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "login": "test login",
                                "password": "test password",
                                "email": "test email",
                                "publishedAt": 1714320006.512803000,
                                "lastLogin": 1714320006.512803000
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.login").value("test login"));
    }

    @Test
    @DisplayName("user controller: update")
    @Sql("/db/insert-test-data-into-user-table.sql")
    void update_returnsUpdatedUserDto_whenDataIsValid() throws Exception {
        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "email": "updated email"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("updated email"))
                .andExpect(jsonPath("$.login").value("kvossing0"));
    }

    @Test
    @DisplayName("user controller: delete")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql"})
    void delete_deletesUser_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/posts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("kvossing0"));
    }

    @Test
    @DisplayName("user controller: delete exception")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql"})
    void delete_throwsExceptionNotFound_whenDataIsInvalid() throws Exception {
        mockMvc.perform(delete("/posts/{id}", 4))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("user controller: findAllByRole")
    @Sql("/db/insert-test-data-into-user-table.sql")
    void find_findsAllUsersByRole_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/posts/role").param("roleName", "ROLE_USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].login").value("kvossing0"))
                .andExpect(jsonPath("$[1].login").value("gmaccook1"));
    }

    @Test
    @DisplayName("user controller: findByLogin")
    @Sql("/db/insert-test-data-into-user-table.sql")
    void find_findsUserByLogin_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/posts/login").param("loginName", "kvossing0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("kvossing0"));
    }

    @Test
    @DisplayName("user controller: findAllByJobTitle")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql"})
    void find_findsAllUsersByJobTitle_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/posts/job").param("jobName", "Artificial Intelligence"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].login").value("gmaccook1"));
    }

    @Test
    @DisplayName("user controller: findAllByUniversity")
    @Sql({"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_details-table.sql"})
    void find_findsAllUsersByUniversity_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/users/university").param("universityName", "MIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].login").value("gmaccook1"));
    }
}
