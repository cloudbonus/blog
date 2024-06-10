package com.github.blog.controller;

import com.github.blog.config.ContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
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
@SpringBootTest(classes = ContainerConfig.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = "/db/insert-test-data-into-user-table.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class AuthenticationControllerTests {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @WithAnonymousUser
    @DisplayName("authentication controller: sign up - validation exception")
    void signUp_throwsExceptionValidationFailed_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "username_template",
                                "password": "password_template",
                                "email": "email_template"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Rollback
    @WithAnonymousUser
    @DisplayName("authentication controller: sign up")
    void signUp_returnsUserDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "username_template",
                                "password": "password_Template1!",
                                "email": "email_template@mail.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value("username_template"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("authentication controller: sign in")
    void signIn_returnsToken_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "kvossing0",
                                "password": "Password1!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.username").value("kvossing0"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("authentication controller: signIn - unauthorized exception")
    void signIn_throwsExceptionNotFound_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "username_template",
                                "password": "password_Template1!"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("authentication controller: signIn - validation exception")
    void signIn_throwsExceptionValidationFailed_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "kvossing0",
                                "password": "123"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
