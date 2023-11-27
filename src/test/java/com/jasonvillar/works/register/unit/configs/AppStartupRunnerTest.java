package com.jasonvillar.works.register.unit.configs;

import com.jasonvillar.works.register.configs.AppStartupRunner;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserService;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class AppStartupRunnerTest {
    @Mock
    private Environment environment = Mockito.mock(Environment.class);;

    @Mock
    private UserService userService = Mockito.mock(UserService.class);

    @Mock
    private UserNotValidatedService userNotValidatedService = Mockito.mock(UserNotValidatedService.class);

    @Test
    void givenAppStartupRunner_whenAdminIsDefault_thenShutDownSystem() {
        Mockito.when(userService.getById(1)).thenReturn(
                User.builder()
                        .id(1)
                        .name("Admin")
                        .email("empty_email")
                        .validated(false)
                        .password("admin")
                        .build());

        AppStartupRunner appStartupRunner = new AppStartupRunner(environment, userService, userNotValidatedService);

        ReflectionTestUtils.setField(appStartupRunner, "test", true);

        try {
            appStartupRunner.run(null);
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("0");
        }
    }

    @Test
    void givenAppStartupRunner_whenNewPasswordFind_thenShutDownSystem() {
        Mockito.when(userService.getById(1)).thenReturn(
                User.builder()
                        .id(1)
                        .name("Admin")
                        .email("empty_email")
                        .validated(false)
                        .password("admin")
                        .build());

        Mockito.when(environment.getProperty("admin_password")).thenReturn("NewPasswordForAdminUser");

        AppStartupRunner appStartupRunner = new AppStartupRunner(environment, userService, userNotValidatedService);

        ReflectionTestUtils.setField(appStartupRunner, "test", true);

        try {
            appStartupRunner.run(null);
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("0");
        }
    }

    @Test
    void givenAppStartupRunner_whenNewEmailFind_thenShutDownSystem() {
        User adminUser = User.builder()
                .id(1)
                .name("Admin")
                .email("empty_email")
                .validated(false)
                .password("admin")
                .build();

        Mockito.when(userService.getById(1)).thenReturn(adminUser);

        Mockito.when(environment.getProperty("admin_email")).thenReturn("new@mail.com");

        adminUser.setEmail("new@mail.com");
        adminUser.setValidated(false);
        adminUser.setCode("123456");
        Mockito.when(userService.updateEmailAndGenerateCodeToUser(any(), eq("new@mail.com"))).thenReturn(adminUser);

        AppStartupRunner appStartupRunner = new AppStartupRunner(environment, userService, userNotValidatedService);

        ReflectionTestUtils.setField(appStartupRunner, "test", true);

        try {
            appStartupRunner.run(null);
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("0");
        }
    }

    @Test
    void givenAppStartupRunner_whenAdminEmailIsNotDefaultAndInvalid_thenShutDownSystem() {
        User adminUser = User.builder()
                .id(1)
                .name("Admin")
                .email("new@mail.com")
                .validated(false)
                .password("admin")
                .build();

        Mockito.when(userService.getById(1)).thenReturn(adminUser);

        adminUser.setCode("123456");
        adminUser.setValidated(false);
        Mockito.when(userService.generateCodeToUser(any())).thenReturn(adminUser);

        AppStartupRunner appStartupRunner = new AppStartupRunner(environment, userService, userNotValidatedService);

        ReflectionTestUtils.setField(appStartupRunner, "test", true);

        try {
            appStartupRunner.run(null);
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("0");
        }
    }
}
