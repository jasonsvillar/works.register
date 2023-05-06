package com.jasonvillar.works.register;

import com.jasonvillar.works.register.config.repositories.ContainerInit;
import com.jasonvillar.works.register.config.repositories.Postgres15_2TC;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.assertj.core.api.Assumptions.assumeThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = ContainerInit.class)
class ConnectionTest {
    private static Postgres15_2TC tc = Postgres15_2TC.getInstance();

    @BeforeAll
    static void beforeAll() {
        tc.start();
    }

    @AfterAll
    static void afterAll() {
        tc.stop();
    }

    @Test
    void testConnection() {
        assumeThat(tc.isRunning());
        var connectionProps = new Properties();
        connectionProps.put("user", tc.getUsername());
        connectionProps.put("password", tc.getPassword());

        try (Connection connection = DriverManager.getConnection(tc.getJdbcUrl(),
                connectionProps)) {
            var resultSet = connection.prepareStatement("Select 1").executeQuery();
            resultSet.next();
            Assertions.assertThat(resultSet.getInt(1)).isEqualTo(1);
        } catch (SQLException sqlException) {
            Assertions.assertThat((Exception) sqlException).doesNotThrowAnyException();
        }
    }
}
