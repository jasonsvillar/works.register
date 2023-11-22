package com.jasonvillar.works.register.unit.user.user_not_validated;

import com.jasonvillar.works.register.user.user_not_validated.UserNotValidated;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedId;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedRepository;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class UserNotValidatedServiceTest {
    @Mock
    private UserNotValidatedRepository userNotValidatedRepository;

    @InjectMocks
    private UserNotValidatedService userNotValidatedService;

    @Test
    void makeRandomValidationCode() {
        String code = this.userNotValidatedService.makeRandomValidationCode();
        Assertions.assertThat(code.length()).isEqualTo(6);
        Assertions.assertThat(Integer.valueOf(code).getClass()).hasSameClassAs(Integer.class);
    }

    @Test
    void makeValidationCodeForUserNotValidated() {
        UserNotValidatedId userNotValidatedId = UserNotValidatedId.builder()
                .name("Test User Name")
                .email("test@test.com")
                .build();

        UserNotValidated userNotValidated = UserNotValidated.builder()
                .userNotValidatedId(userNotValidatedId)
                .password("sarasa")
                .build();

        userNotValidated = this.userNotValidatedService.makeValidationCodeForUserNotValidated(userNotValidated);

        Assertions.assertThat(userNotValidated.getCode()).isNotEmpty();
        Assertions.assertThat(userNotValidated.getCode()).isNotNull();
    }

    @Test
    void save() {
        UserNotValidatedId userNotValidatedId = UserNotValidatedId.builder()
                .name("Test User Name")
                .email("test@test.com")
                .build();

        UserNotValidated userNotValidated = UserNotValidated.builder()
                .userNotValidatedId(userNotValidatedId)
                .password("sarasa")
                .build();

        Mockito.when(this.userNotValidatedRepository.save(eq(userNotValidated))).thenReturn(userNotValidated);

        UserNotValidated userNotValidatedReturned = this.userNotValidatedService.save(userNotValidated);

        Assertions.assertThat(userNotValidatedReturned.getUserNotValidatedId().getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void findOptionalById() {
        UserNotValidatedId userNotValidatedId = UserNotValidatedId.builder()
                .name("Test User Name")
                .email("test@test.com")
                .build();

        UserNotValidated userNotValidated = UserNotValidated.builder()
                .userNotValidatedId(userNotValidatedId)
                .password("sarasa")
                .build();

        Mockito.when(this.userNotValidatedRepository.findById(eq(userNotValidatedId))).thenReturn(Optional.of(userNotValidated));

        Optional<UserNotValidated> userNotValidatedReturned = this.userNotValidatedService.findOptionalById(userNotValidatedId);

        Assertions.assertThat(userNotValidatedReturned.get()).isEqualTo(userNotValidated);
    }

    @Test
    void findOptionalByIdAndCode() {
        UserNotValidatedId userNotValidatedId = UserNotValidatedId.builder()
                .name("Test User Name")
                .email("test@test.com")
                .build();

        UserNotValidated userNotValidated = UserNotValidated.builder()
                .userNotValidatedId(userNotValidatedId)
                .password("sarasa")
                .code("123456")
                .build();

        Mockito.when(this.userNotValidatedRepository.findByUserNotValidatedIdAndCode(eq(userNotValidatedId), eq("123456"))).thenReturn(Optional.of(userNotValidated));

        Optional<UserNotValidated> userNotValidatedReturned = this.userNotValidatedService.findOptionalByIdAndCode(userNotValidatedId, "123456");

        Assertions.assertThat(userNotValidatedReturned.get()).isEqualTo(userNotValidated);
    }

    @Test
    void deleteById() {
        UserNotValidatedId userNotValidatedId = UserNotValidatedId.builder()
                .name("Test User Name")
                .email("test@test.com")
                .build();

        UserNotValidated userNotValidated = UserNotValidated.builder()
                .userNotValidatedId(userNotValidatedId)
                .password("sarasa")
                .code("123456")
                .build();

        Mockito.when(this.userNotValidatedRepository.deleteByUserNotValidatedId(eq(userNotValidatedId))).thenReturn(1L);

        boolean deleted = this.userNotValidatedService.deleteById(userNotValidatedId);

        Assertions.assertThat(deleted).isTrue();
    }
}
