package com.jasonvillar.works.register.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jasonvillar.works.register.Application;
import com.jasonvillar.works.register.authentication.port.in.AuthenticationRequest;
import com.jasonvillar.works.register.email.EmailService;
import com.jasonvillar.works.register.unit.configs_for_tests.repositories.ContainerInit;
import com.jasonvillar.works.register.unit.configs_for_tests.repositories.Postgres15_2TC;
import com.jasonvillar.works.register.user.port.in.UserRequest;
import com.jasonvillar.works.register.user.port.out.UserDTO;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.ArgumentMatchers.anyString;
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

    @AfterEach
    void afterEach(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "work_register");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "service");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "client");
    }

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public EmailService emailService;

    @MockBean
    public UserNotValidatedService userNotValidatedService;

    @BeforeEach
    void beforeEach() {
        Mockito.when(emailService.sendSimpleMessage(anyString(), anyString(), anyString())).thenReturn(true);
    }

    public ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    public ResultActions doGetRequestAndGetResultActions(String uri) throws Exception {
        return this.mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        );
    }

    public ResultActions doGetRequestWithJWTAndGetResultActions(String uri, String jwt) throws Exception {
        return this.mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .header("Authorization", "Bearer ".concat(jwt))
        );
    }

    public ResultActions doPostRequestAndGetResultActions(String uri, String requestJson) throws Exception {
        return this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .with(csrf())
        );
    }

    public ResultActions doPostRequestWithJWTAndGetResultActions(String uri, String requestJson, String jwt) throws Exception {
        return this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .with(csrf())
                .header("Authorization", "Bearer ".concat(jwt))
        );
    }

    /*--------------------------------------------------------------*/

    public String doPostRequest(String uri, String requestJson, ResultMatcher resultMatcher) throws Exception {
        MvcResult result = this.doPostRequestAndGetResultActions(uri, requestJson)
                .andExpect(resultMatcher)
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    public String doPostRequestWithJWT(String uri, String requestJson, ResultMatcher resultMatcher, String jwt) throws Exception {
        MvcResult result = this.doPostRequestWithJWTAndGetResultActions(uri, requestJson, jwt)
                .andExpect(resultMatcher)
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    public String doGetRequest(String uri, ResultMatcher resultMatcher) throws Exception {
        MvcResult result = this.doGetRequestAndGetResultActions(uri)
                .andExpect(resultMatcher)
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    public String doGetRequestWithJWT(String uri, ResultMatcher resultMatcher, String jwt) throws Exception {
        MvcResult result = this.doGetRequestWithJWTAndGetResultActions(uri, jwt)
                .andExpect(resultMatcher)
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    public String loginAndGetJWT(AuthenticationRequest authenticationRequest) throws Exception {
        String requestJson = ow.writeValueAsString(authenticationRequest);

        ResultActions resultActions = this.doPostRequestAndGetResultActions("/api/auth/basic-authentication", requestJson);

        return resultActions.andReturn().getResponse().getHeader("authorization");
    }

    public String loginAsAdminAndGetJWT() throws Exception {
        return this.loginAndGetJWT(new AuthenticationRequest("Admin", "admin"));
    }

    public void logoutJWT(String jwt) throws Exception {
        this.doGetRequestWithJWT("/api/auth/logout-jwt", status().isOk(), jwt);
    }

    public UserDTO saveUser(UserRequest userRequest, String jwt) throws Exception {
        String requestJson = ow.writeValueAsString(userRequest);

        String response = this.doPostRequestWithJWT("/api/v1/user", requestJson, status().isCreated() ,jwt);

        return mapper.readValue(response, UserDTO.class);
    }
}
