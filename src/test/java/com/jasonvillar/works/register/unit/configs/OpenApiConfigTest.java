package com.jasonvillar.works.register.unit.configs;

import com.jasonvillar.works.register.configs.OpenApiConfig;
import io.swagger.v3.oas.models.OpenAPI;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

class OpenApiConfigTest {
    @Test
    void openApiConfigTest() {
        OpenApiConfig openApiConfig = new OpenApiConfig();
        OpenAPI openAPI = openApiConfig.customOpenAPI();

        Assertions.assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.2.1");
    }
}
