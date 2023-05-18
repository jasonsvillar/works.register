package com.jasonvillar.works.register.repositories;

import com.jasonvillar.works.register.configtests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.entities.Client;
import com.jasonvillar.works.register.entities.User;
import com.jasonvillar.works.register.entities.UserClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Optional;

class UserClientRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    UserClientRepository userClientRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClientRepository clientRepository;

    UserClient userClientInDatabase = UserClient
            .builder()
            .build();

    private User userInDatabase = User.builder()
            .name("Test for UserClient Name")
            .email("test.userclient1@gmail.com")
            .build();

    private Client clientInDatabase = Client.builder()
            .name("Test for UserClient Name")
            .surname("Test for UserClient Surname")
            .dni("11111111")
            .build();

    @BeforeEach
    void setUp() {
        this.userInDatabase = this.userRepository.save(this.userInDatabase);
        this.clientInDatabase = this.clientRepository.save(this.clientInDatabase);

        userClientInDatabase.setUserId(userInDatabase.getId());
        userClientInDatabase.setClientId(clientInDatabase.getId());

        this.userClientInDatabase = this.userClientRepository.save(this.userClientInDatabase);
    }

    @AfterEach
    void setDown(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_client");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "\"user\"");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "client");
    }

    @Test
    void givenUserClient_WhenSave_thenCheckIfNotNull() {
        UserClient userClient = UserClient.builder()
                .userId(userInDatabase.getId())
                .clientId(clientInDatabase.getId())
                .build();

        UserClient userClientSaved = this.userClientRepository.save(userClient);
        Assertions.assertThat(userClientSaved).isNotNull();
    }

    @Test
    void givenUserClient_WhenFindUserClientById_thenCheckIfNotNull() {
        UserClient userClientSaved = this.userClientRepository.findUserClientById(this.userClientInDatabase.getId());
        Assertions.assertThat(userClientSaved).isNotNull();
    }

    @Test
    void givenUserClient_WhenFindUserClientById_thenCheckUserIdAndClientId() {
        UserClient userClient = this.userClientRepository.findUserClientById(this.userClientInDatabase.getId());

        Assertions.assertThat(userClient.getUserId()).isEqualTo(userInDatabase.getId());
        Assertions.assertThat(userClient.getClientId()).isEqualTo(clientInDatabase.getId());
    }

    @Test
    void givenUserClientInTable_whenFindOptionalById_thenIsPresentAssertionsTrueAndFalse() {
        Optional<UserClient> userClient = this.userClientRepository.findOptionalById(this.userClientInDatabase.getId());
        Assertions.assertThat(userClient).isPresent();

        userClient = this.userClientRepository.findOptionalById(0);
        Assertions.assertThat(userClient).isNotPresent();
    }

    @Test
    void givenUserClientInTable_whenFindOptionalByUserIdAndClientId_thenIsPresentAssertionsTrueAndFalse() {
        Optional<UserClient> userClient = this.userClientRepository.findOptionalByUserIdAndClientId(
                this.userClientInDatabase.getUserId(),
                this.userClientInDatabase.getClientId()
        );
        Assertions.assertThat(userClient).isPresent();

        userClient = null;

        userClient = this.userClientRepository.findOptionalByUserIdAndClientId(0, 0);
        Assertions.assertThat(userClient).isNotPresent();

        userClient = null;

        userClient = this.userClientRepository.findOptionalByUserIdAndClientId(1, 0);
        Assertions.assertThat(userClient).isNotPresent();

        userClient = null;

        userClient = this.userClientRepository.findOptionalByUserIdAndClientId(0, 1);
        Assertions.assertThat(userClient).isNotPresent();
    }
}
