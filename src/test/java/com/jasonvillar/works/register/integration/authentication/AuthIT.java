package com.jasonvillar.works.register.integration.authentication;

import com.jasonvillar.works.register.integration.IntegrationTestsConfig;
import com.jasonvillar.works.register.authentication.port.in.AuthenticationRequest;
import com.jasonvillar.works.register.authentication.port.out.AuthenticationResponse;
import org.assertj.core.api.Assertions;
import org.springframework.http.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthIT extends IntegrationTestsConfig {
    @Test
    void Given_adminLoggedCanGetUsers_When_logout_Then_cantGetUsers() throws Exception {
        String requestJson = ow.writeValueAsString(new AuthenticationRequest("Admin", "admin"));
        MvcResult result = this.mockMvc.perform(post("/api/auth/basic-authentication").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        AuthenticationResponse authenticationResponse = mapper.readValue(response, AuthenticationResponse.class);

        String jwt = authenticationResponse.accessToken();

        // Login success
        // Request Users

        this.mockMvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .header("Authorization", "Bearer ".concat(jwt))
                )
                .andExpect(status().isOk());

        // Get Users
        // Logout

        this.mockMvc.perform(get("/api/auth/logout-jwt").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .header("Authorization", "Bearer ".concat(jwt))
                )
                .andExpect(status().isOk());

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
