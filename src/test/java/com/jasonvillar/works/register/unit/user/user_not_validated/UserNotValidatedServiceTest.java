package com.jasonvillar.works.register.unit.user.user_not_validated;

import com.jasonvillar.works.register.email.EmailService;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidated;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedId;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedRepository;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class UserNotValidatedServiceTest {
    @Mock
    private UserNotValidatedRepository userNotValidatedRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserNotValidatedService userNotValidatedService;

    @Test
    void makeRandomValidationCode() {
        String code = this.userNotValidatedService.makeRandomValidationCode();
        Assertions.assertThat(code).hasSize(6);
        Assertions.assertThat(Integer.valueOf(code).getClass()).hasSameClassAs(Integer.class);
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

        Mockito.when(this.userNotValidatedRepository.save(userNotValidated)).thenReturn(userNotValidated);

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

        Mockito.when(this.userNotValidatedRepository.findById(userNotValidatedId)).thenReturn(Optional.of(userNotValidated));

        Optional<UserNotValidated> userNotValidatedReturned = this.userNotValidatedService.findOptionalById(userNotValidatedId);

        Assertions.assertThat(userNotValidatedReturned).contains(userNotValidated);
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

        Mockito.when(this.userNotValidatedRepository.findByUserNotValidatedIdAndCode(userNotValidatedId, "123456")).thenReturn(Optional.of(userNotValidated));

        Optional<UserNotValidated> userNotValidatedReturned = this.userNotValidatedService.findOptionalByIdAndCode(userNotValidatedId, "123456");

        Assertions.assertThat(userNotValidatedReturned).contains(userNotValidated);
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

        Mockito.when(this.userNotValidatedRepository.deleteByUserNotValidatedId(userNotValidatedId)).thenReturn(1L);

        boolean deleted = this.userNotValidatedService.deleteById(userNotValidatedId);

        Assertions.assertThat(deleted).isTrue();

        Mockito.when(this.userNotValidatedRepository.deleteByUserNotValidatedId(userNotValidatedId)).thenReturn(0L);

        deleted = this.userNotValidatedService.deleteById(userNotValidatedId);

        Assertions.assertThat(deleted).isFalse();
    }

    @Test
    void sendValidationCode() {
        Mockito.when(this.emailService.sendSimpleMessage(anyString(), anyString(), anyString())).thenReturn(true);
        boolean send = this.userNotValidatedService.sendValidationCode("Admin", "test@test.com", "123456", null, true);
        Assertions.assertThat(send).isTrue();

        Mockito.when(this.emailService.sendSimpleMessage(anyString(), anyString(), anyString())).thenReturn(true);
        send = this.userNotValidatedService.sendValidationCode("Admin", "test@test.com", "123456", null, false);
        Assertions.assertThat(send).isTrue();

        Mockito.when(this.emailService.sendSimpleMessage(anyString(), anyString(), anyString())).thenReturn(true);
        send = this.userNotValidatedService.sendValidationCode("Admin", "test@test.com", "123456", "http://frontend/code", true);
        Assertions.assertThat(send).isTrue();

        Mockito.when(this.emailService.sendSimpleMessage(anyString(), anyString(), anyString())).thenReturn(true);
        send = this.userNotValidatedService.sendValidationCode("Admin", "test@test.com", "123456", "http://frontend/code", false);
        Assertions.assertThat(send).isTrue();
    }
}
