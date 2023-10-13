package com.jasonvillar.works.register.repositories;

import com.jasonvillar.works.register.configtests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.entities.Privilege;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

class PrivilegeRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    PrivilegeRepository repository;

    private Privilege privilegeInDatabase = Privilege.builder()
            .name("Test Privilege")
            .build();

    @BeforeEach
    void setUp() {
        this.repository.deleteAll();
        this.privilegeInDatabase = this.repository.save(this.privilegeInDatabase);
    }

    @Test
    void givenEntity_WhenSave_thenCheckIfNotNull() {
        Privilege entity = Privilege.builder()
                .name("New Privilege")
                .build();

        Privilege privilegeSaved = this.repository.save(entity);
        Assertions.assertThat(privilegeSaved).isNotNull();
    }

    @Test
    void givenEntityInTable_whenFindAll_thenCheckIfEmpty() {
        List<Privilege> entityList = this.repository.findAll();
        Assertions.assertThat(entityList).isNotEmpty();
    }

    @Test
    void givenEntityInTable_whenFindClientById_thenCheckNameIsTestPrivilege() {
        Privilege entity = this.repository.findPrivilegeById(this.privilegeInDatabase.getId());
        Assertions.assertThat(entity.getName()).isEqualTo("Test Privilege");
    }

    @Test
    void givenEntityInTable_whenFindOptionalById_thenIsPresentAssertionsTrueAndFalse() {
        Optional<Privilege> optional = this.repository.findOptionalById(this.privilegeInDatabase.getId());
        Assertions.assertThat(optional).isPresent();

        optional = this.repository.findOptionalById(0L);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenEntityInTable_whenFindOptionalByName_thenIsPresentAssertionsTrueAndFalse() {
        Optional<Privilege> optional = this.repository.findOptionalByName(this.privilegeInDatabase.getName());
        Assertions.assertThat(optional).isPresent();

        optional = this.repository.findOptionalByName("Non existent name");
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenEntityInTable_whenFindAllByNameContainingIgnoreCase_thenCheckIfEmpty() {
        List<Privilege> entity = this.repository.findAllByNameContainingIgnoreCase("test");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByNameContainingIgnoreCase("privilege");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByNameContainingIgnoreCase("ivile");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.repository.findAllByNameContainingIgnoreCase("Non existent name");
        Assertions.assertThat(entity).isEmpty();
    }
}
