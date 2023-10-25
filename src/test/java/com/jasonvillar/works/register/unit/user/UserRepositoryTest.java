package com.jasonvillar.works.register.unit.user;

import com.jasonvillar.works.register.unit.configs_for_tests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Optional;

class UserRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    UserRepository userRepository;

    private User userInDatabase = User.builder()
            .name("Dummy")
            .email("dummy@dummy.com")
            .build();

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_service");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "client");
        this.userRepository.findAll().forEach(user -> user.getHasRoleList().forEach(user::removeRole));
        this.userRepository.deleteAll();
        this.userInDatabase = this.userRepository.save(this.userInDatabase);
    }

    @Test
    void givenUser_WhenSave_thenCheckIfNotNull() {
        String password = new BCryptPasswordEncoder().encode("yo");

        User user = User.builder()
                .name("test")
                .email("test@test.com")
                .password(password)
                .build();

        User userSaved = userRepository.save(user);
        Assertions.assertThat(userSaved).isNotNull();
    }

    @Test
    void givenUserInTable_whenFindAll_thenCheckIfEmpty() {
        List<User> userList = userRepository.findAll();

        Assertions.assertThat(userList).isNotEmpty();
    }

    @Test
    void givenUserInTable_whenFindUserById_thenCheckNameIsDummy() {
        User user = this.userRepository.findUserById(this.userInDatabase.getId());

        Assertions.assertThat(user.getName()).isEqualTo("Dummy");
    }

    @Test
    void givenUserInTable_whenFindOptionalById_thenIsPresentAssertionsTrueAndFalse() {
        Optional<User> user = this.userRepository.findOptionalById(this.userInDatabase.getId());
        Assertions.assertThat(user).isPresent();

        user = this.userRepository.findOptionalById(0);
        Assertions.assertThat(user).isNotPresent();
    }

    @Test
    void givenUserInTable_whenFindOptionalByName_thenIsPresentAssertionsTrueAndFalse() {
        Optional<User> user = this.userRepository.findOptionalByName(this.userInDatabase.getName());
        Assertions.assertThat(user).isPresent();

        user = this.userRepository.findOptionalByName("Nonexistent name");
        Assertions.assertThat(user).isNotPresent();
    }

    @Test
    void givenUserInTable_whenFindOptionalByEmail_thenIsPresentAssertionsTrueAndFalse() {
        Optional<User> user = this.userRepository.findOptionalByEmail(this.userInDatabase.getEmail());
        Assertions.assertThat(user).isPresent();

        user = this.userRepository.findOptionalByName("Nonexistent email");
        Assertions.assertThat(user).isNotPresent();
    }

    @Test
    void givenUserInTable_whenFindAllByNameContainingIgnoreCase_thenCheckIfEmpty() {
        List<User> userList = this.userRepository.findAllByNameContainingIgnoreCase(this.userInDatabase.getName());
        Assertions.assertThat(userList).isNotEmpty();

        userList = this.userRepository.findAllByNameContainingIgnoreCase("Nonexistent name");
        Assertions.assertThat(userList).isEmpty();
    }

    @Test
    void givenUserInTable_whenFindAllByEmailContainingIgnoreCase_thenCheckIfEmpty() {
        List<User> userList = this.userRepository.findAllByEmailContainingIgnoreCase(this.userInDatabase.getEmail());
        Assertions.assertThat(userList).isNotEmpty();

        userList = this.userRepository.findAllByEmailContainingIgnoreCase("Nonexistent email");
        Assertions.assertThat(userList).isEmpty();
    }

    @Test
    void givenUserInTable_whenFindAllByNameContainingIgnoreCaseAndEmailContainingIgnoreCase_thenCheckIfEmpty() {
        List<User> userList = this.userRepository.findAllByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(this.userInDatabase.getName(), this.userInDatabase.getEmail());
        Assertions.assertThat(userList).isNotEmpty();

        userList = this.userRepository.findAllByNameContainingIgnoreCaseAndEmailContainingIgnoreCase("Nonexistent name", "Nonexistent email");
        Assertions.assertThat(userList).isEmpty();
    }
}
