package com.jasonvillar.works.register.repositories;

import com.jasonvillar.works.register.authentication.PrivilegeRepository;
import com.jasonvillar.works.register.authentication.RoleRepository;
import com.jasonvillar.works.register.configtests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.authentication.Privilege;
import com.jasonvillar.works.register.authentication.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

class RoleRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;

    private Role roleInDatabase = Role.builder()
            .name("Test Role")
            .build();

    private Privilege privilegeInDatabase = Privilege.builder()
            .name("Test Privilege")
            .build();

    @BeforeEach
    void setUp() {
        this.roleRepository.deleteAll();
        this.privilegeRepository.deleteAll();

        this.privilegeInDatabase = this.privilegeRepository.save(this.privilegeInDatabase);
        roleInDatabase.setHasPrivilegeList(List.of(privilegeInDatabase));

        this.roleInDatabase = this.roleRepository.save(roleInDatabase);
    }

    @Test
    void givenEntity_WhenSave_thenCheckIfNotNull() {
        Role role = Role.builder()
                .name("New Role")
                .hasPrivilegeList(List.of(privilegeInDatabase))
                .build();

        Role roleSaved = this.roleRepository.save(role);
        Assertions.assertThat(roleSaved).isNotNull();
    }

    @Test
    void givenEntityInTable_whenFindAll_thenCheckIfEmpty() {
        List<Role> entityList = this.roleRepository.findAll();
        Assertions.assertThat(entityList).isNotEmpty();
    }

    @Test
    void givenEntityInTable_whenFindRoleById_thenCheckNameIsTestRole() {
        Role entity = this.roleRepository.findRoleById(this.roleInDatabase.getId());
        Assertions.assertThat(entity.getName()).isEqualTo("Test Role");
    }

    @Test
    void givenEntityInTable_whenFindOptionalById_thenIsPresentAssertionsTrueAndFalse() {
        Optional<Role> optional = this.roleRepository.findOptionalById(this.roleInDatabase.getId());
        Assertions.assertThat(optional).isPresent();

        optional = this.roleRepository.findOptionalById(0);
        Assertions.assertThat(optional).isNotPresent();
    }

    @Test
    void givenEntityInTable_whenFindAllByNameContainingIgnoreCase_thenCheckIfEmpty() {
        List<Role> entity = this.roleRepository.findAllByNameContainingIgnoreCase("Test Role");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.roleRepository.findAllByNameContainingIgnoreCase("test role");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.roleRepository.findAllByNameContainingIgnoreCase("est rol");
        Assertions.assertThat(entity).isNotEmpty();

        entity = this.roleRepository.findAllByNameContainingIgnoreCase("Non existent name");
        Assertions.assertThat(entity).isEmpty();
    }
}
