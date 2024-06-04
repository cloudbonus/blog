package com.github.blog.controller;

import com.github.blog.config.ControllerTestConfig;
import com.github.blog.config.WebTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Raman Haurylau
 */
@Transactional
@SpringJUnitWebConfig(classes = {ControllerTestConfig.class, WebTestConfig.class,})
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = {"/db/insert-test-data-into-user-table.sql", "/db/insert-test-data-into-post-table.sql", "/db/insert-test-data-into-order-table.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, scripts = "/db/clean-test-data.sql")
public class OrderControllerTests {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @Rollback
    @WithUserDetails("company")
    @DisplayName("order controller: reserve")
    void reserve_returnsReservedOrderDto_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/orders/{id}/reserve", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("RESERVED"));
    }

    @Test
    @Rollback
    @WithUserDetails("company")
    @DisplayName("order controller: cancel")
    void cancel_returnsCanceledOrderDto_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/orders/{id}/reserve", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("RESERVED"));

        mockMvc.perform(get("/orders/{id}/cancel", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("CANCELED"));
    }

    @Test
    @Rollback
    @WithUserDetails("company")
    @DisplayName("order controller: buy")
    void buy_returnsPurchasedOrderDto_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/orders/{id}/reserve", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("RESERVED"));

        mockMvc.perform(get("/orders/{id}/buy", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("COMPLETED"));
    }


    @Test
    @Rollback
    @WithUserDetails("admin")
    @DisplayName("order controller: update")
    void update_returnsUpdatedOrderDto_whenDataIsValid() throws Exception {
        mockMvc.perform(put("/orders/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "postId": 4,
                                "userId": 5
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    @Rollback
    @WithUserDetails("admin")
    @DisplayName("order controller: delete")
    void delete_returnsDeletedOrderDto_whenDataIsValid() throws Exception {
        mockMvc.perform(delete("/orders/{id}", 4))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    @WithUserDetails("company")
    @DisplayName("order controller: find by id")
    void find_findsOrderById_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/orders/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    @WithUserDetails("company")
    @DisplayName("order controller: find all")
    void find_findsAllOrdersByPostIdAndUserId_whenDataIsValid() throws Exception {
        mockMvc.perform(get("/orders").param("postId", "5").param("userId", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }
}
