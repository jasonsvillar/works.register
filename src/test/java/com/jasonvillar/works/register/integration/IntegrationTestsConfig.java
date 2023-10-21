package com.jasonvillar.works.register.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jasonvillar.works.register.Application;
import com.jasonvillar.works.register.unit.configs_for_tests.repositories.ContainerInit;
import com.jasonvillar.works.register.unit.configs_for_tests.repositories.Postgres15_2TC;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

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
}
