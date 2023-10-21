package com.jasonvillar.works.register.unit.user_service;

import com.jasonvillar.works.register.unit.configs_for_tests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.service.Service;
import com.jasonvillar.works.register.service.ServiceRepository;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserRepository;
import com.jasonvillar.works.register.user_service.UserService;
import com.jasonvillar.works.register.user_service.UserServiceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Optional;

class UserServiceRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    UserServiceRepository userServiceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ServiceRepository serviceRepository;

    UserService userServiceInDatabase = UserService.builder().build();

    private User userInDatabase = User.builder()
            .name("Test for UserClient Name")
            .email("test.userclient1@gmail.com")
            .build();

    private Service serviceInDatabase = Service.builder()
            .name("Dummy name")
            .build();

    @BeforeEach
    void setUp() {
        this.userInDatabase = this.userRepository.save(this.userInDatabase);
        this.serviceInDatabase = this.serviceRepository.save(this.serviceInDatabase);

        this.userServiceInDatabase.setUserId(this.userInDatabase.getId());
        this.userServiceInDatabase.setServiceId(this.serviceInDatabase.getId());

        this.userServiceInDatabase = this.userServiceRepository.save(this.userServiceInDatabase);
    }

    @AfterEach
    void setDown(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_service");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_role");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "\"user\"");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "service");
    }

    @Test
    void givenUserClient_WhenSave_thenCheckIfNotNull() {
        UserService userService = UserService.builder()
                .userId(this.userInDatabase.getId())
                .serviceId(this.serviceInDatabase.getId())
                .build();

        UserService userServiceSaved = this.userServiceRepository.save(userService);
        Assertions.assertThat(userServiceSaved).isNotNull();
    }

    @Test
    void givenUserServiceInTable_WhenFindUserServiceById_thenCheckUserIdAndClientId() {
        UserService userService = this.userServiceRepository.findUserServiceById(this.userServiceInDatabase.getId());

        Assertions.assertThat(userService.getUserId()).isEqualTo(this.userInDatabase.getId());
        Assertions.assertThat(userService.getServiceId()).isEqualTo(this.serviceInDatabase.getId());
    }

    @Test
    void givenUserServiceInTable_whenFindOptionalById_thenIsPresentAssertionsTrueAndFalse() {
        Optional<UserService> userService = this.userServiceRepository.findOptionalById(this.userServiceInDatabase.getId());
        Assertions.assertThat(userService).isPresent();

        userService = this.userServiceRepository.findOptionalById(0);
        Assertions.assertThat(userService).isNotPresent();
    }

    @Test
    void givenUserServiceInTable_whenFindOptionalByUserIdAndServiceId_thenIsPresentAssertionsTrueAndFalse() {
        Optional<UserService> userService = this.userServiceRepository.findOptionalByUserIdAndServiceId(
                this.userServiceInDatabase.getUserId(),
                this.userServiceInDatabase.getServiceId()
        );
        Assertions.assertThat(userService).isPresent();

        userService = null;

        userService = this.userServiceRepository.findOptionalByUserIdAndServiceId(0, 0);
        Assertions.assertThat(userService).isNotPresent();

        userService = null;

        userService = this.userServiceRepository.findOptionalByUserIdAndServiceId(1, 0);
        Assertions.assertThat(userService).isNotPresent();

        userService = null;

        userService = this.userServiceRepository.findOptionalByUserIdAndServiceId(0, 1);
        Assertions.assertThat(userService).isNotPresent();
    }

    @Test
    void givenUserServiceInTable_whenFindAllByUserId_thenCheckIfEmpty() {
        List<UserService> userService = this.userServiceRepository.findAllByUserId(userInDatabase.getId());
        Assertions.assertThat(userService).isNotEmpty();

        userService = this.userServiceRepository.findAllByUserId(0);
        Assertions.assertThat(userService).isEmpty();
    }

    @Test
    void givenUserServiceInTable_whenFindAllByServiceId_thenCheckIfEmpty() {
        List<UserService> userService = this.userServiceRepository.findAllByServiceId(serviceInDatabase.getId());
        Assertions.assertThat(userService).isNotEmpty();

        userService = this.userServiceRepository.findAllByServiceId(0);
        Assertions.assertThat(userService).isEmpty();
    }
}
