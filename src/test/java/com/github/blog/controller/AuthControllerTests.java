package com.github.blog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Raman Haurylau
 */
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringJUnitWebConfig(WebTestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthControllerTests {
    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("auth controller: signUp")
    @WithAnonymousUser
    @Sql("/db/controllertests/insert-test-data-into-user-table.sql")
    void signUp_returnsUserDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "username_template",
                                "password": "password_template",
                                "email": "email_template"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.username").value("username_template"));
    }

    @Test
    @DisplayName("auth controller: signIn")
    @WithMockUser("kvossing0")
    @Sql("/db/controllertests/insert-test-data-into-user-table.sql")
    void signIn_returnsToken_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "kvossing0",
                                "password": "123"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("auth controller: signIn exception")
    @WithAnonymousUser
    void signIn_throwsExceptionNotFound_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "username_template",
                                "password": "password_template"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
