package com.jasonvillar.works.register.integration.authentication;

import com.jasonvillar.works.register.integration.IntegrationTestsConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthIT extends IntegrationTestsConfig {
    @Test
    void Given_adminLoggedCanGetUsers_When_logout_Then_cantGetUsers() throws Exception {
        String jwt = this.loginAsAdminAndGetJWT();

        this.doGetRequestWithJWT("/api/v1/users", status().isOk(), jwt);

        this.logoutJWT(jwt);

        int status = this.doGetRequestWithJWTAndGetResultActions("/api/v1/users", jwt).andReturn().getResponse().getStatus();

        Assertions.assertThat(status).isNotEqualTo(200);
    }
}
