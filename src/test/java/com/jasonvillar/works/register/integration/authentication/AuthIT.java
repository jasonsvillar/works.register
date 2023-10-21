package com.jasonvillar.works.register.integration.authentication;

import com.jasonvillar.works.register.integration.IntegrationTestsConfig;
import org.assertj.core.api.Assertions;
import org.springframework.http.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthIT extends IntegrationTestsConfig {
    @Test
    void Given_adminLoggedCanGetUsers_When_logout_Then_cantGetUsers() throws Exception {
        MvcResult result;

        String jwt = this.loginAsAdminAndGetJWT();

        // Login success
        // Request Users

        this.mockMvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .header("Authorization", "Bearer ".concat(jwt))
                )
                .andExpect(status().isOk());

        // Get Users
        // Logout

        this.logoutJWT(jwt);

        // Logout success
        // Request Users

        result = this.mockMvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .header("Authorization", "Bearer ".concat(jwt))
                )
                .andReturn();

        int status = result.getResponse().getStatus();

        Assertions.assertThat(status).isNotEqualTo(200);
    }
}
