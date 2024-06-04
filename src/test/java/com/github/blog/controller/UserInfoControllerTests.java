package com.github.blog.controller;

import com.github.blog.config.ControllerTestConfig;
import com.github.blog.config.WebTestConfig;
import com.github.blog.service.security.impl.UserDetailsImpl;
import com.github.blog.service.statemachine.state.UserInfoState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
@SpringJUnitWebConfig(classes = {ControllerTestConfig.class, WebTestConfig.class,})
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-user_info-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class UserInfoControllerTests {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @Rollback
    @WithUserDetails("admin")
    @DisplayName("user info controller: create")
    void createAndCancel_returnsUserInfoDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/user-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstname": "firstname",
                                "surname": "surname",
                                "university": "university",
                                "major": "major"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());

        mockMvc.perform(get("/user-info/{id}/cancel", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(UserInfoState.CANCELED.name()));
    }

    @Test
    @Rollback
    @DisplayName("user info controller: create")
    void createAndVerify_returnsUserInfoDto_whenDataIsValid() throws Exception {
        UserDetailsImpl user = new UserDetailsImpl(4L, "user", "Password1!", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        UserDetailsImpl admin = new UserDetailsImpl(3L, "admin", "Password1!", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(post("/user-info")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstname": "firstname",
                                "surname": "surname",
                                "university": "university",
                                "major": "major"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());

        mockMvc.perform(get("/user-info/{id}/verify", 1).param("roleId", "3")
                        .with(user(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(UserInfoState.VERIFIED.name()));
    }

    @Test
    @WithUserDetails
    @DisplayName("user info controller: create - validation exception")
    void create_throwValidationException_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/user-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstname": "firstname_template",
                                "surname": "surname_template"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Rollback
    @WithUserDetails("admin")
    @DisplayName("user info controller: update")
    void update_returnsUpdatedUserInfoDto_whenDataIsValid() throws Exception {
        mockMvc.perform(put("/user-info/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "university": "university",
                                "major": "major"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithUserDetails("kvossing0")
    @DisplayName("user info controller: update - forbidden exception")
    void update_throwExceptionForbidden_whenDataIsInvalid() throws Exception {
        mockMvc.perform(put("/user-info/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstname": "firstname",
                                "surname": "surname",
                                "university": "university",
                                "major": "major"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Rollback
    @WithUserDetails("admin")
    @DisplayName("user info controller: delete")
    void delete_deletesUserInfo_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/user-info/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    @WithUserDetails("kvossing0")
    @DisplayName("user info controller: delete - bad request exception")
    void delete_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(delete("/user-info/{id}", 2))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("kvossing0")
    @DisplayName("user info controller: find by id")
    void findById_findsUserInfoById_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/user-info/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithUserDetails("kvossing0")
    @DisplayName("user info controller: find by id - bad request exception")
    void findById_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(get("/user-info/{id}", 2))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("user info controller: find all")
    void find_findsAllUserInfo_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/user-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @WithUserDetails("kvossing0")
    @DisplayName("user info controller: find all - bad request exception")
    void find_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(get("/user-info"))
                .andExpect(status().isBadRequest());
    }
}
