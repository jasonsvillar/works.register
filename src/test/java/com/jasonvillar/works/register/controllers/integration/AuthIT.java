package com.jasonvillar.works.register.controllers.integration;

import com.jasonvillar.works.register.Application;
import com.jasonvillar.works.register.configtests.repositories.ContainerInit;
import com.jasonvillar.works.register.configtests.repositories.Postgres15_2TC;
import com.jasonvillar.works.register.dto.security.AuthenticationRequest;
import org.json.JSONException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

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
    public void testBasicAuth() throws JSONException {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("sarasaMan", "a Weak Password");

        HttpEntity<AuthenticationRequest> authenticationRequestLogin = new HttpEntity<>(authenticationRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/auth/basic-authentication"),
                HttpMethod.POST, authenticationRequestLogin, String.class);

        String bearer = response.getBody();

        //JSONAssert.assertEquals(null, response.getBody(), false);
    }
}
