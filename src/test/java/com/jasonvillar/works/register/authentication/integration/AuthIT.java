package com.jasonvillar.works.register.authentication.integration;

import com.jasonvillar.works.register.Application;
import com.jasonvillar.works.register.configs_for_tests.repositories.ContainerInit;
import com.jasonvillar.works.register.configs_for_tests.repositories.Postgres15_2TC;
import com.jasonvillar.works.register.authentication.port.in.AuthenticationRequest;
import com.jasonvillar.works.register.authentication.port.out.AuthenticationResponse;
import com.jasonvillar.works.register.user.port.out.UserDTO;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = ContainerInit.class)
class AuthIT {
    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    private static Postgres15_2TC tc = Postgres15_2TC.getInstance();

    @BeforeAll
    static void beforeAll() {
        tc.start();
    }

    @AfterAll
    static void afterAll() {
        tc.stop();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    void testBasicAuth() throws JSONException {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("Admin", "admin");

        HttpEntity<AuthenticationRequest> authenticationRequestLogin = new HttpEntity<>(authenticationRequest, headers);

        ResponseEntity<AuthenticationResponse> authenticationPost = restTemplate.exchange(
                createURLWithPort("/api/auth/basic-authentication"),
                HttpMethod.POST, authenticationRequestLogin, AuthenticationResponse.class);

        AuthenticationResponse authenticationResponse = authenticationPost.getBody();

        Assertions.assertThat(authenticationResponse).hasFieldOrProperty("accessToken");

        // Login success
        // Request Users

        String jwt = authenticationResponse.accessToken();
        headers.setBearerAuth(jwt);

        HttpEntity<String> jwtHeader = new HttpEntity<>(null, headers);

        ResponseEntity< List<UserDTO> > userGet = restTemplate.exchange(
                createURLWithPort("/api/v1/users"),
                HttpMethod.GET, jwtHeader, new ParameterizedTypeReference<>(){}
        );

        List<UserDTO> userResponse = userGet.getBody();

        Assertions.assertThat(userResponse).isNotEmpty();

        // Get Users
        // Logout

        ResponseEntity<String> logoutGet = restTemplate.exchange(
                createURLWithPort("/api/auth/logout-jwt"),
                HttpMethod.GET, jwtHeader, String.class
        );

        String logoutResponse = logoutGet.getBody();

        Assertions.assertThat(logoutResponse).isEqualTo("Logout success");

        // Logout success
        // Request Users

        userGet = restTemplate.exchange(
                createURLWithPort("/api/v1/users"),
                HttpMethod.GET, jwtHeader, new ParameterizedTypeReference<>(){}
        );

        userResponse = userGet.getBody();

        Assertions.assertThat(userResponse).isNull();

        // No Users because of the logout
    }
}
