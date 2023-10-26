package com.jasonvillar.works.register.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WORKS REGISTER")
                        .version("1.0")
                        .description("Spring Boot 3 API REST")
                        .termsOfService("")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org"))
                ).components(new Components().addSecuritySchemes("jwt-bearer-token",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER).name("Authorization")));
    }
}
