package com.jasonvillar.works.register.unit.service;

import com.jasonvillar.works.register.unit.configs_for_tests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.service.Service;
import com.jasonvillar.works.register.service.ServiceRepository;
import com.jasonvillar.works.register.user_service.UserService;
import com.jasonvillar.works.register.user_service.UserServiceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Optional;

class ServiceRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    UserServiceRepository userServiceRepository;

    private Service serviceInDatabase = Service.builder()
            .name("Dummy name")
            .build();

    private UserService userServiceInDatabase = UserService.builder()
            .userId(1)
            .serviceId(0)
            .build();

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_service");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "service");
        this.serviceInDatabase = this.serviceRepository.save(this.serviceInDatabase);
        this.userServiceInDatabase.setServiceId(this.serviceInDatabase.getId());
        this.userServiceInDatabase = this.userServiceRepository.save(this.userServiceInDatabase);
    }

    @Test
    void givenService_whenSave_thenCheckIfNotNull() {
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
    void givenServiceInTable_whenFindAllPageable_thenCheckIfEmpty() {
        Pageable page = PageRequest.of(0, 10);
        Page<Service> servicePage = this.serviceRepository.findAll(page);

        Assertions.assertThat(servicePage).isNotEmpty();
    }

    @Test
    void givenServiceInTable_whenFindAllByUserServiceListUserId_thenCheckIfEmpty() {
        Pageable page = PageRequest.of(0, 10);
        List<Service> serviceList = this.serviceRepository.findAllByUserServiceListUserId(1, page);

        Assertions.assertThat(serviceList).isNotEmpty();
    }

    @Test
    void givenServiceInTable_whenCountByUserServiceListUserId_thenCheckRowCountIsGreaterThan0() {
        long rowCount = this.serviceRepository.countByUserServiceListUserId(1);

        Assertions.assertThat(rowCount).isGreaterThan(0);
    }

    @Test
    void givenServiceInTable_whenCount_thenCheckRowContIsGreaterThan0() {
        long rowCount = this.serviceRepository.count();

        Assertions.assertThat(rowCount).isGreaterThan(0);
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

    @Test
    void givenServiceInTable_whenFindAllByNameContainingIgnoreCaseAndUserServiceListUserId_thenCheckIfEmpty() {
        List<Service> serviceList = this.serviceRepository.findAllByNameContainingIgnoreCaseAndUserServiceListUserId("dummy Name", 1);
        Assertions.assertThat(serviceList).isNotEmpty();

        serviceList = this.serviceRepository.findAllByNameContainingIgnoreCaseAndUserServiceListUserId("dummy Name", 1);
        Assertions.assertThat(serviceList).isNotEmpty();

        serviceList = this.serviceRepository.findAllByNameContainingIgnoreCaseAndUserServiceListUserId("dummy Name", 0);
        Assertions.assertThat(serviceList).isEmpty();
    }

    @Test
    void givenServiceInTable_whenFindOptionalByIdAndUserServiceListUserId_thenIsPresentAssertionsTrue() {
        Optional<Service> service = this.serviceRepository.findOptionalByIdAndUserServiceListUserId(this.serviceInDatabase.getId(), 1);

        Assertions.assertThat(service).isPresent();
    }
}
