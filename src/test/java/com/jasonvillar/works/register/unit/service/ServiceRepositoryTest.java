package com.jasonvillar.works.register.unit.service;

import com.jasonvillar.works.register.client.Client;
import com.jasonvillar.works.register.client.ClientRepository;
import com.jasonvillar.works.register.unit.configs_for_tests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.service.Service;
import com.jasonvillar.works.register.service.ServiceRepository;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.work_register.WorkRegister;
import com.jasonvillar.works.register.work_register.WorkRegisterRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

class ServiceRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    WorkRegisterRepository workRegisterRepository;

    @Autowired
    ClientRepository clientRepository;

    User adminUser = User.builder().id(1L).name("Admin").validated(true).build();

    private Service serviceInDatabase = Service.builder()
            .name("Dummy name")
            .user(this.adminUser)
            .build();

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "service");
        this.serviceInDatabase = this.serviceRepository.save(this.serviceInDatabase);
    }

    @Test
    void givenService_whenSave_thenCheckIfNotNull() {
        Service service = Service.builder()
                .name("test")
                .user(this.adminUser)
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
    void givenServiceInTable_whenFindAllByUserId_thenCheckIfEmpty() {
        Pageable page = PageRequest.of(0, 10);
        List<Service> serviceList = this.serviceRepository.findAllByUserId(1, page);

        Assertions.assertThat(serviceList).isNotEmpty();
    }

    @Test
    void givenServiceInTable_whenCountByUserId_thenCheckIsPositive() {
        long rowCount = this.serviceRepository.countByUserId(1);

        Assertions.assertThat(rowCount).isPositive();
    }

    @Test
    void givenServiceInTable_whenCount_thenCheckIsPositive() {
        long rowCount = this.serviceRepository.count();

        Assertions.assertThat(rowCount).isPositive();
    }

    @Test
    void givenServiceInTable_whenFindAllByNameContainingIgnoreCaseAndUserId_thenCheckIfEmpty() {
        List<Service> serviceList = this.serviceRepository.findAllByNameContainingIgnoreCaseAndUserId("dummy Name", 1);
        Assertions.assertThat(serviceList).isNotEmpty();

        serviceList = this.serviceRepository.findAllByNameContainingIgnoreCaseAndUserId("dummy Name", 1);
        Assertions.assertThat(serviceList).isNotEmpty();

        serviceList = this.serviceRepository.findAllByNameContainingIgnoreCaseAndUserId("dummy surname", 1);
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
    void givenServiceInTable_whenFindOptionalByNameAndUserId_thenIsNotPresent() {
        Optional<Service> service = this.serviceRepository.findOptionalByNameAndUserId(this.serviceInDatabase.getName(), 1);
        Assertions.assertThat(service).isPresent();

        service = this.serviceRepository.findOptionalByNameAndUserId("Nonexistent name", 1);
        Assertions.assertThat(service).isNotPresent();
    }

    @Test
    void specificationName() {
        Specification<Service> specificationName = ServiceRepository.containsName("umMy Nam");

        List<Service> serviceList = serviceRepository.findAll(specificationName);

        Assertions.assertThat(serviceList).isNotEmpty();
    }

    @Test
    void specificationUserId() {
        Specification<Service> specificationUserId = ServiceRepository.equalsUserId(this.serviceInDatabase.getUser().getId());

        List<Service> serviceList = serviceRepository.findAll(specificationUserId);

        Assertions.assertThat(serviceList).isNotEmpty();
    }

    @Test
    void specificationId() {
        Specification<Service> specificationId = ServiceRepository.equalsId(this.serviceInDatabase.getId());

        List<Service> serviceList = serviceRepository.findAll(specificationId);

        Assertions.assertThat(serviceList).isNotEmpty();
    }

    @Test
    void specificationNameAndIdAndUserIdWithPage() {
        Pageable page = PageRequest.of(0, 5, Sort.by("name"));

        Specification<Service> specificationName = ServiceRepository.containsName("umMy Nam");
        Specification<Service> specificationId = ServiceRepository.equalsId(this.serviceInDatabase.getId());
        Specification<Service> specificationUserId = ServiceRepository.equalsUserId(this.serviceInDatabase.getUser().getId());

        Specification<Service> allSpecifications =
                specificationName
                .and(specificationId)
                .and(specificationUserId);

        List<Service> serviceList = serviceRepository.findAll(allSpecifications, page).toList();

        Assertions.assertThat(serviceList).isNotEmpty();


        Specification<Service> specifications = ServiceRepository.equalsUserId(this.serviceInDatabase.getUser().getId());
        specifications = specifications.and(ServiceRepository.equalsId(this.serviceInDatabase.getId()));
        specifications = specifications.and(ServiceRepository.containsName("umMy Nam"));

        serviceList = serviceRepository.findAll(specifications, page).toList();

        Assertions.assertThat(serviceList).isNotEmpty();
    }

    @Test
    void findAllByIdListAndUserIdAndServiceNotInWorkRegister() {
        Client client = Client.builder()
                .user(this.adminUser)
                .identificationNumber("11222333")
                .name("Client name")
                .surname("Client surname")
                .build();

        client = clientRepository.save(client);

        Service service2 = this.serviceRepository.save(Service.builder().name("Service 2").user(this.adminUser).build());

        workRegisterRepository.save(
          WorkRegister.builder()
                  .serviceId(service2.getId())
                  .userId(this.adminUser.getId())
                  .title("Some work")
                  .dateFrom(LocalDate.now())
                  .timeFrom(LocalTime.now())
                  .dateTo(LocalDate.now())
                  .timeTo(LocalTime.now())
                  .payment(BigDecimal.valueOf(100))
                  .clientId(client.getId())
                  .build()
        );

        List<Service> serviceList = serviceRepository.findAllByIdListAndUserIdAndServiceNotInWorkRegister(List.of(this.serviceInDatabase.getId(), service2.getId()), 1L);

        Assertions.assertThat(serviceList).hasSize(1);
    }
}
