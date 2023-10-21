package com.jasonvillar.works.register.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jasonvillar.works.register.Application;
import com.jasonvillar.works.register.authentication.port.in.AuthenticationRequest;
import com.jasonvillar.works.register.authentication.port.out.AuthenticationResponse;
import com.jasonvillar.works.register.unit.configs_for_tests.repositories.ContainerInit;
import com.jasonvillar.works.register.unit.configs_for_tests.repositories.Postgres15_2TC;
import com.jasonvillar.works.register.user.port.in.UserRequest;
import com.jasonvillar.works.register.user.port.out.UserDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = ContainerInit.class)
public class IntegrationTestsConfig {
    private static Postgres15_2TC tc = Postgres15_2TC.getInstance();

    @BeforeAll
    static void beforeAll() {
        tc.start();
    }

    @AfterAll
    static void afterAll() {
        tc.stop();
    }

    @Autowired
    public MockMvc mockMvc;

    public ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    public String loginAsAdminAndGetJWT() throws Exception {
        String requestJson = ow.writeValueAsString(new AuthenticationRequest("Admin", "admin"));
        MvcResult result = this.mockMvc.perform(post("/api/auth/basic-authentication").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        AuthenticationResponse authenticationResponse = mapper.readValue(response, AuthenticationResponse.class);

        return authenticationResponse.accessToken();
    }

    public void logoutJWT(String jwt) throws Exception {
        this.mockMvc.perform(get("/api/auth/logout-jwt").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .header("Authorization", "Bearer ".concat(jwt))
                )
                .andExpect(status().isOk());
    }

    public UserDTO saveUser(UserRequest userRequest) throws Exception {
        String requestJson = ow.writeValueAsString(userRequest);
        MvcResult result = this.mockMvc.perform(post("/api/v1/user").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        return mapper.readValue(response, UserDTO.class);
    }
}
