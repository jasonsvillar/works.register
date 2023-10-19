package com.jasonvillar.works.register.client;

import com.jasonvillar.works.register.configs_for_tests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.user.User;
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

    private User userInDatabase = User.builder()
            .name("Dummy name")
            .email("Dummy surname")
            .id(1)
            .build();

    private Client clientInDatabase = Client.builder()
            .name("Dummy name")
            .surname("Dummy surname")
            .identificationNumber("11222333")
            .user(this.userInDatabase)
            .build();

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "client");
        this.clientInDatabase = this.repository.save(this.clientInDatabase);
    }

    @Test
    void givenEntity_whenSave_thenCheckIfNotNull() {
        Client entity = Client.builder()
                .name("test")
                .surname("test")
                .identificationNumber("00000000")
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
    void givenEntityInTable_whenFindOptionalByIdentificationNumber_thenIsPresentAssertionsTrueAndFalse() {
        Optional<Client> optional = this.repository.findOptionalByIdentificationNumber(this.clientInDatabase.getIdentificationNumber());
        Assertions.assertThat(optional).isPresent();

        optional = this.repository.findOptionalByIdentificationNumber("00000000");
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
    void givenEntityInTable_whenFindAllByIdentificationNumberContainingIgnoreCase_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllByIdentificationNumberContainingIgnoreCase("11222333");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByIdentificationNumberContainingIgnoreCase("122233");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByIdentificationNumberContainingIgnoreCase("000");
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
        List<Client> entity = this.repository.findAllByUserId(this.userInDatabase.getId());
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByUserId(0);
        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    void givenEntityInTable_whenFindOptionalByIdAndUserClientListUserId_thenCheckIfPresent() {
        Optional<Client> entity = this.repository.findOptionalByIdAndUserId(clientInDatabase.getId(), this.userInDatabase.getId());
        Assertions.assertThat(entity).isPresent();

        entity = this.repository.findOptionalByIdAndUserId(clientInDatabase.getId(), 0);
        Assertions.assertThat(entity).isNotPresent();
    }

    @Test
    void givenEntityInTable_whenFindAllByNameContainingIgnoreCaseAndUserClientListUserId_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllByNameContainingIgnoreCaseAndUserId("dummy nam", this.userInDatabase.getId());
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByNameContainingIgnoreCaseAndUserId("dummy nam", 0);
        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    void givenEntityInTable_whenFindAllBySurnameContainingIgnoreCaseAndUserClientListUserId_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllBySurnameContainingIgnoreCaseAndUserId("dummy surnam", this.userInDatabase.getId());
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllBySurnameContainingIgnoreCaseAndUserId("dummy surnam", 0);
        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    void givenEntityInTable_whenFindAllByIdentificationNumberContainingIgnoreCaseAndUserClientListUserId_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllByIdentificationNumberContainingIgnoreCaseAndUserId(this.clientInDatabase.getIdentificationNumber(), this.userInDatabase.getId());
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByIdentificationNumberContainingIgnoreCaseAndUserId(this.clientInDatabase.getIdentificationNumber(), 0);
        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    void givenEntityInTable_whenFindAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndUserClientListUserId_thenCheckIfEmpty() {
        List<Client> entity = this.repository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndUserId("dummy nam", "dummy surnam", this.userInDatabase.getId());
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndUserId("dummy nam", "dummy surnam", 0);
        Assertions.assertThat(entity).isEmpty();
    }
}
