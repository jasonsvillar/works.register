package com.jasonvillar.works.register.integration.service;

import com.jasonvillar.works.register.integration.IntegrationTestsConfig;
import com.jasonvillar.works.register.user.port.in.UserRequest;
import com.jasonvillar.works.register.user.port.out.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

public class ServiceIT extends IntegrationTestsConfig {
    @Test
    void Given_2users_When_theySaveServices_Then_canGetOwnedServices() throws Exception {
        MvcResult result;

        UserDTO userDTO = this.saveUser(new UserRequest("test1", "test1@gmail.com", "test1"));

        String AdminJWT = this.loginAsAdminAndGetJWT();
    }
}
