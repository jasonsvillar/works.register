package com.jasonvillar.works.register.unit.user.user_not_validated;

import com.jasonvillar.works.register.unit.configs_for_tests.repositories.DataJpaTestTemplate;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidated;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedId;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.assertj.core.api.Assertions;

class UserNotValidatedRepositoryTest extends DataJpaTestTemplate {
    @Autowired
    UserNotValidatedRepository userNotValidatedRepository;

    @Test
    void deleteByUserNotValidatedId() {
        UserNotValidatedId userNotValidatedId = UserNotValidatedId.builder()
                .name("Test User Name")
                .email("test@test.com")
                .build();

        UserNotValidated userNotValidated = UserNotValidated.builder()
                .userNotValidatedId(userNotValidatedId)
                .code("123456")
                .password("sarasa")
                .build();

        this.userNotValidatedRepository.save(userNotValidated);

        long rowsDeleted = this.userNotValidatedRepository.deleteByUserNotValidatedId(userNotValidatedId);

        Assertions.assertThat(rowsDeleted).isPositive();
    }
}
