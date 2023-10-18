package com.jasonvillar.works.register.client;

import com.jasonvillar.works.register.configs_for_tests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.user_client.UserClient;
import com.jasonvillar.works.register.user_client.UserClientRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Optional;

class ClientRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    ClientRepository repository;

    @Autowired
    UserClientRepository userClientRepository;

    private Client clientInDatabase = Client.builder()
            .name("Dummy name")
            .surname("Dummy surname")
            .dni("11222333")
            .build();

    private UserClient userClientInDatabase = UserClient.builder()
            .userId(1)
            .clientId(0)
            .build();

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_client");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "client");
        this.clientInDatabase = this.repository.save(this.clientInDatabase);
        this.userClientInDatabase.setClientId(clientInDatabase.getId());
        this.userClientInDatabase = this.userClientRepository.save(this.userClientInDatabase);
    }

    @Test
    void givenEntity_whenSave_thenCheckIfNotNull() {
        Client entity = Client.builder()
                .name("test")
                .surname("test")
                .dni("00000000")
                .build();

        Client clientSaved = this.repository.save(entity);
        Assertions.assertThat(clientSaved).isNotNull();
    }

    @Test
    void givenEntityInTable_whenFindAll_thenCheckIfEmpty() {
        List<Client> entityList = this.repository.findAll();
        Assertions.assertThat(entityList).isNotEmpty();
    }

    @Test
    void givenEntityInTable_whenFindClientById_thenCheckNameIsDummy() {
        Client entity = this.repository.findClientById(this.clientInDatabase.getId());
        Assertions.assertThat(entity.getName()).isEqualTo("Dummy name");
    }

    @Test
    void givenEntityInTable_whenFindOptionalById_thenIsPresentAssertionsTrueAndFalse() {
        Optional<Client> optional = this.repository.findOptionalById(this.clientInDatabase.getId());
        Assertions.assertThat(optional).isPresent();

        optional = this.repository.findOptionalById(0);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenEntityInTable_whenFindOptionalByDni_thenIsPresentAssertionsTrueAndFalse() {
        Optional<Client> optional = this.repository.findOptionalByDni(this.clientInDatabase.getDni());
        Assertions.assertThat(optional).isPresent();

        optional = this.repository.findOptionalByDni("00000000");
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenEntityInTable_whenFindAllByNameContainingIgnoreCase_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllByNameContainingIgnoreCase("dummy");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByNameContainingIgnoreCase("name");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByNameContainingIgnoreCase("ummy");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByNameContainingIgnoreCase("Nonexistent name");
        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    void givenEntityInTable_whenFindAllBySurnameContainingIgnoreCase_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllBySurnameContainingIgnoreCase("dummy");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllBySurnameContainingIgnoreCase("surname");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllBySurnameContainingIgnoreCase("ummy");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllBySurnameContainingIgnoreCase("Nonexistent surname");
        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    void givenEntityInTable_whenFindAllByDniContainingIgnoreCase_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllByDniContainingIgnoreCase("11222333");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByDniContainingIgnoreCase("122233");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByDniContainingIgnoreCase("000");
        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    void givenEntityInTable_whenFindAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase("dummy nam", "ummy Surn");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase("dummy nam", "Nonexistent surname");
        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    void givenEntityInTable_whenFindAllByUserClientListUserId_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllByUserClientListUserId(this.userClientInDatabase.getUserId());
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByUserClientListUserId(0);
        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    void givenEntityInTable_whenFindOptionalByIdAndUserClientListUserId_thenCheckIfPresent() {
        Optional<Client> entity = this.repository.findOptionalByIdAndUserClientListUserId(clientInDatabase.getId(), this.userClientInDatabase.getUserId());
        Assertions.assertThat(entity).isPresent();

        entity = this.repository.findOptionalByIdAndUserClientListUserId(clientInDatabase.getId(), 0);
        Assertions.assertThat(entity).isNotPresent();
    }

    @Test
    void givenEntityInTable_whenFindAllByNameContainingIgnoreCaseAndUserClientListUserId_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllByNameContainingIgnoreCaseAndUserClientListUserId("dummy nam", this.userClientInDatabase.getUserId());
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByNameContainingIgnoreCaseAndUserClientListUserId("dummy nam", 0);
        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    void givenEntityInTable_whenFindAllBySurnameContainingIgnoreCaseAndUserClientListUserId_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllBySurnameContainingIgnoreCaseAndUserClientListUserId("dummy surnam", this.userClientInDatabase.getUserId());
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllBySurnameContainingIgnoreCaseAndUserClientListUserId("dummy surnam", 0);
        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    void givenEntityInTable_whenFindAllByDniContainingIgnoreCaseAndUserClientListUserId_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllByDniContainingIgnoreCaseAndUserClientListUserId(this.clientInDatabase.getDni(), this.userClientInDatabase.getUserId());
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByDniContainingIgnoreCaseAndUserClientListUserId(this.clientInDatabase.getDni(), 0);
        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    void givenEntityInTable_whenFindAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndUserClientListUserId_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndUserClientListUserId("dummy nam", "dummy surnam", this.userClientInDatabase.getUserId());
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndUserClientListUserId("dummy nam", "dummy surnam", 0);
        Assertions.assertThat(entity).isEmpty();
    }
}
