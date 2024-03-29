package com.jasonvillar.works.register.unit.configs_for_tests.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;

public class ContainerInit implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static Postgres15_2TC tc;

    static {
        tc = Postgres15_2TC.getInstance();
        tc.start();
    }

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + tc.getJdbcUrl(),
                "spring.datasource.username=" + tc.getUsername(),
                "spring.datasource.password=" + tc.getPassword(),
                "db.host=" + tc.getHost(),
                "db.port=" + tc.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                "db.name=" + tc.getDatabaseName(),
                "db.username=" + tc.getUsername(),
                "db.password=" + tc.getPassword()
        );
    }
}

