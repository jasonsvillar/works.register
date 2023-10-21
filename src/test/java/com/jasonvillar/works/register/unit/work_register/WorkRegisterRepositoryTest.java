package com.jasonvillar.works.register.unit.work_register;

import com.jasonvillar.works.register.client.Client;
import com.jasonvillar.works.register.client.ClientRepository;
import com.jasonvillar.works.register.unit.configs_for_tests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.service.Service;
import com.jasonvillar.works.register.service.ServiceRepository;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserRepository;
import com.jasonvillar.works.register.work_register.WorkRegister;
import com.jasonvillar.works.register.work_register.WorkRegisterRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

class WorkRegisterRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    WorkRegisterRepository workRegisterRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ClientRepository clientRepository;

    private User userInDatabase = User.builder()
            .name("User dummy name")
            .email("test.userclient1@gmail.com")
            .build();

    private Service serviceInDatabase = Service.builder()
            .name("Service dummy name")
            .build();

    private Client clientInDatabase = Client.builder()
            .name("Client dummy name")
            .surname("Client dummy surname")
            .identificationNumber("11222333")
            .build();

    private WorkRegister workRegisterInDatabase = WorkRegister.builder()
            .title("Dummy title")
            .dateFrom(new Jsr310JpaConverters.LocalDateConverter().convertToEntityAttribute(new Date()))
            .timeFrom(new Jsr310JpaConverters.LocalTimeConverter().convertToEntityAttribute(new Date()))
            .dateTo(new Jsr310JpaConverters.LocalDateConverter().convertToEntityAttribute(new Date()))
            .timeTo(new Jsr310JpaConverters.LocalTimeConverter().convertToEntityAttribute(new Date()))
            .payment(new BigDecimal(1500))
            .build();

    @BeforeEach
    void setUp() {
        this.userInDatabase = this.userRepository.save(this.userInDatabase);
        this.serviceInDatabase = this.serviceRepository.save(this.serviceInDatabase);

        this.clientInDatabase.setUserId(this.userInDatabase.getId());
        this.clientInDatabase = this.clientRepository.save(this.clientInDatabase);

        this.workRegisterInDatabase.setUserId(this.userInDatabase.getId());
        this.workRegisterInDatabase.setServiceId(this.serviceInDatabase.getId());
        this.workRegisterInDatabase.setClientId(this.clientInDatabase.getId());

        this.workRegisterInDatabase = this.workRegisterRepository.save(this.workRegisterInDatabase);
    }

    @AfterEach
    void setDown(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "work_register");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "client");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_role");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "\"user\"");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "service");
    }

    @Test
    void givenWorkRegister_WhenSave_thenCheckIfNotNull() {
        WorkRegister workRegister = WorkRegister.builder()
                .title("Dummy title")
                .dateFrom(new Jsr310JpaConverters.LocalDateConverter().convertToEntityAttribute(new Date()))
                .timeFrom(new Jsr310JpaConverters.LocalTimeConverter().convertToEntityAttribute(new Date()))
                .dateTo(new Jsr310JpaConverters.LocalDateConverter().convertToEntityAttribute(new Date()))
                .timeTo(new Jsr310JpaConverters.LocalTimeConverter().convertToEntityAttribute(new Date()))
                .payment(new BigDecimal(1500))
                .userId(userInDatabase.getId())
                .serviceId(serviceInDatabase.getId())
                .clientId(clientInDatabase.getId())
                .build();

        WorkRegister workRegisterSaved = this.workRegisterRepository.save(workRegister);
        Assertions.assertThat(workRegisterSaved).isNotNull();
    }

    @Test
    void givenWorkRegisterInTable_WhenFindWorkRegisterById_thenCheckUserIdAndClientId() {
        WorkRegister workRegister = this.workRegisterRepository.findWorkRegisterById(this.workRegisterInDatabase.getId());

        Assertions.assertThat(workRegister.getUserId()).isEqualTo(this.userInDatabase.getId());
        Assertions.assertThat(workRegister.getServiceId()).isEqualTo(this.serviceInDatabase.getId());
        Assertions.assertThat(workRegister.getClientId()).isEqualTo(this.clientInDatabase.getId());
    }

    @Test
    void givenWorkRegisterInTable_whenFindOptionalById_thenIsPresentAssertionsTrueAndFalse() {
        Optional<WorkRegister> workRegister = this.workRegisterRepository.findOptionalById(this.workRegisterInDatabase.getId());
        Assertions.assertThat(workRegister).isPresent();

        workRegister = this.workRegisterRepository.findOptionalById(0);
        Assertions.assertThat(workRegister).isNotPresent();
    }

    @Test
    void givenWorkRegisterInTable_whenFindAllByUserId_thenCheckIfEmpty() {
        List<WorkRegister> workRegister = this.workRegisterRepository.findAllByUserId(userInDatabase.getId());
        Assertions.assertThat(workRegister).isNotEmpty();

        workRegister = this.workRegisterRepository.findAllByUserId(0);
        Assertions.assertThat(workRegister).isEmpty();
    }

    @Test
    void givenWorkRegisterInTable_whenFindAllByClientId_thenCheckIfEmpty() {
        List<WorkRegister> workRegister = this.workRegisterRepository.findAllByClientId(clientInDatabase.getId());
        Assertions.assertThat(workRegister).isNotEmpty();

        workRegister = this.workRegisterRepository.findAllByClientId(0);
        Assertions.assertThat(workRegister).isEmpty();
    }

    @Test
    void givenWorkRegisterInTable_whenFindAllByServiceId_thenCheckIfEmpty() {
        List<WorkRegister> workRegister = this.workRegisterRepository.findAllByServiceId(serviceInDatabase.getId());
        Assertions.assertThat(workRegister).isNotEmpty();

        workRegister = this.workRegisterRepository.findAllByServiceId(0);
        Assertions.assertThat(workRegister).isEmpty();
    }

    @Test
    void givenWorkRegisterInTable_whenFindAllByUserIdAndClientId_thenCheckIfEmpty() {
        List<WorkRegister> workRegister = this.workRegisterRepository.findAllByUserIdAndClientId(userInDatabase.getId(), clientInDatabase.getId());
        Assertions.assertThat(workRegister).isNotEmpty();

        workRegister = this.workRegisterRepository.findAllByUserIdAndClientId(0,0);
        Assertions.assertThat(workRegister).isEmpty();

        workRegister = this.workRegisterRepository.findAllByUserIdAndClientId(0,1);
        Assertions.assertThat(workRegister).isEmpty();

        workRegister = this.workRegisterRepository.findAllByUserIdAndClientId(1,0);
        Assertions.assertThat(workRegister).isEmpty();
    }

    @Test
    void givenWorkRegisterInTable_whenFindAllByTitleContainingIgnoreCase_thenCheckIfEmpty() {
        List<WorkRegister> workRegister = this.workRegisterRepository.findAllByTitleContainingIgnoreCase("dummy Title");
        Assertions.assertThat(workRegister).isNotEmpty();

        workRegister = this.workRegisterRepository.findAllByTitleContainingIgnoreCase("ummy");
        Assertions.assertThat(workRegister).isNotEmpty();

        workRegister = this.workRegisterRepository.findAllByTitleContainingIgnoreCase("itl");
        Assertions.assertThat(workRegister).isNotEmpty();

        workRegister = this.workRegisterRepository.findAllByTitleContainingIgnoreCase("Nonexistent name");
        Assertions.assertThat(workRegister).isEmpty();
    }

    /*---------------------------------------------------*/

    @Test
    void givenWorkRegisterInTable_whenFindOptionalByIdAndUserId_thenIsPresentAssertionsTrueAndFalse() {
        Optional<WorkRegister> workRegister = this.workRegisterRepository.findOptionalByIdAndUserId(this.workRegisterInDatabase.getId(), this.userInDatabase.getId());
        Assertions.assertThat(workRegister).isPresent();

        workRegister = this.workRegisterRepository.findOptionalByIdAndUserId(this.workRegisterInDatabase.getId(), 0);
        Assertions.assertThat(workRegister).isNotPresent();
    }

    @Test
    void givenWorkRegisterInTable_whenFindAllByTitleContainingIgnoreCaseAndUserId_thenCheckIfEmpty() {
        List<WorkRegister> workRegister = this.workRegisterRepository.findAllByTitleContainingIgnoreCaseAndUserId("ummy", userInDatabase.getId());
        Assertions.assertThat(workRegister).isNotEmpty();

        workRegister = this.workRegisterRepository.findAllByTitleContainingIgnoreCaseAndUserId("ummy", 0);
        Assertions.assertThat(workRegister).isEmpty();
    }
}
