package com.jasonvillar.works.register.repositories;

import com.jasonvillar.works.register.config.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.entities.Client;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

class ClientRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    ClientRepository repository;

    private Client clientInDatabase = Client.builder()
            .name("Dummy name")
            .surname("Dummy surname")
            .dni("11222333")
            .build();

    @BeforeEach
    void setUp() {
        this.repository.deleteAll();
        this.clientInDatabase = this.repository.save(this.clientInDatabase);
    }

    @Test
    void givenEntity_WhenSave_thenCheckIfNotNull() {
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
        List<Client> a = this.repository.findAll();
        Assertions.assertThat(a).isNotEmpty();
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
}
