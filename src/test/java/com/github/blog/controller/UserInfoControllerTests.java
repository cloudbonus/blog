package com.github.blog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
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
public class UserInfoControllerTests {
    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("userInfo controller: create")
    @WithUserDetails
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql"})
    void create_returnsUserInfoDto_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/user-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id": 4,
                                "firstname": "firstname_template",
                                "surname": "surname_template"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.firstname").value("firstname_template"));
    }

    @Test
    @DisplayName("userInfo controller: create exception")
    @WithUserDetails
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql"})
    void create_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(post("/user-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id": 1,
                                "firstname": "firstname_template",
                                "surname": "surname_template"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("userInfo controller: update")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql"})
    void update_returnsUpdatedUserInfoDto_whenDataIsValid() throws Exception {
        mockMvc.perform(put("/user-info/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstname": "firstname_template"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstname").value("firstname_template"));
    }

    @Test
    @DisplayName("userInfo controller: update exception")
    @WithUserDetails("kvossing0")
    @Sql("/db/controllertests/insert-test-data-into-user-table.sql")
    void update_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(put("/user-info/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstname": "firstname_template"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("userInfo controller: delete")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql"})
    void delete_deletesUserInfo_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/user-info/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    @DisplayName("userInfo controller: delete exception")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql"})
    void delete_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(delete("/user-info/{id}", 2))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("userInfo controller: findById")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql"})
    void findById_findsUserInfoById_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/user-info/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("userInfo controller: findById exception")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql"})
    void findById_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(get("/user-info/{id}", 2))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("userInfo controller: findAll")
    @WithUserDetails("admin")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql"})
    void find_findsAllUserInfo_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/user-info"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("userInfo controller: findAll exception")
    @WithUserDetails("kvossing0")
    @Sql({"/db/controllertests/insert-test-data-into-user-table.sql", "/db/controllertests/insert-test-data-into-user_details-table.sql"})
    void find_throwExceptionForbidden_whenDataNotBelongToUser() throws Exception {
        mockMvc.perform(get("/user-info"))
                .andExpect(status().isBadRequest());
    }

}
