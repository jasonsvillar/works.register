package com.jasonvillar.works.register.repositories;

import com.jasonvillar.works.register.configtests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.entities.Service;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

class ServiceRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    ServiceRepository serviceRepository;

    private Service serviceInDatabase = Service.builder()
            .name("Dummy name")
            .build();

    @BeforeEach
    void setUp() {
        this.serviceRepository.deleteAll();
        this.serviceInDatabase = this.serviceRepository.save(this.serviceInDatabase);
    }

    @Test
    void givenService_WhenSave_thenCheckIfNotNull() {
        Service service = Service.builder()
                .name("test")
                .build();

        Service serviceSaved = this.serviceRepository.save(service);

        Assertions.assertThat(serviceSaved).isNotNull();
    }

    @Test
    void givenServiceInTable_whenFindAll_thenCheckIfEmpty() {
        List<Service> serviceList = this.serviceRepository.findAll();

        Assertions.assertThat(serviceList).isNotEmpty();
    }

    @Test
    void givenServiceInTable_whenFindAllByNameContainingIgnoreCase_thenCheckIfEmpty() {
        List<Service> serviceList = this.serviceRepository.findAllByNameContainingIgnoreCase("dummy Name");
        Assertions.assertThat(serviceList).isNotEmpty();

        serviceList = this.serviceRepository.findAllByNameContainingIgnoreCase("dummy Name");
        Assertions.assertThat(serviceList).isNotEmpty();

        serviceList = this.serviceRepository.findAllByNameContainingIgnoreCase("dummy surname");
        Assertions.assertThat(serviceList).isEmpty();
    }

    @Test
    void givenServiceInTable_whenFindServiceById_thenCheckNameIsDummy() {
        Service service = this.serviceRepository.findServiceById(this.serviceInDatabase.getId());

        Assertions.assertThat(service.getName()).isEqualTo("Dummy name");
    }

    @Test
    void givenServiceInTable_whenFindOptionalById_thenIsPresentAssertionsTrueAndFalse() {
        Optional<Service> service = this.serviceRepository.findOptionalById(this.serviceInDatabase.getId());
        Assertions.assertThat(service).isPresent();

        service = this.serviceRepository.findOptionalById(0);
        Assertions.assertThat(service).isNotPresent();
    }

    @Test
    void givenServiceInTable_whenFindOptionalByName_thenIsPresentAssertionsTrueAndFalse() {
        Optional<Service> service = this.serviceRepository.findOptionalByName(this.serviceInDatabase.getName());
        Assertions.assertThat(service).isPresent();

        service = this.serviceRepository.findOptionalByName("Nonexistent name");
        Assertions.assertThat(service).isNotPresent();
    }
}
