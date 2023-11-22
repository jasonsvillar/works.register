package com.jasonvillar.works.register.unit.user.user_not_validated;

import com.jasonvillar.works.register.email.EmailService;
import com.jasonvillar.works.register.unit.configs_for_tests.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserService;
import com.jasonvillar.works.register.user.port.out.UserDTOAdapter;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidated;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedController;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedId;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedService;
import com.jasonvillar.works.register.user.user_not_validated.port.in.UserNotValidatedRequest;
import com.jasonvillar.works.register.user.user_not_validated.port.in.UserNotValidatedRequestAdapter;
import com.jasonvillar.works.register.user.user_not_validated.port.out.UserNotValidatedDTOAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {UserNotValidatedController.class, UserNotValidatedRequestAdapter.class, UserNotValidatedDTOAdapter.class, UserDTOAdapter.class})
public class UserNotValidatedControllerTest extends ControllerTestTemplate {
    @MockBean
    private UserNotValidatedService userNotValidatedService;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    void savePreUser() throws Exception {
        UserNotValidatedRequest userNotValidatedRequest = new UserNotValidatedRequest(
                "Test user name",
                "test@test.com",
                "sarasa",
                "http://localhost/frontend/url"
        );

        String requestJson = ow.writeValueAsString(userNotValidatedRequest);

        UserNotValidatedId userNotValidatedId = UserNotValidatedId.builder()
                .name("Test user name")
                .email("test@test.com")
                .build();

        UserNotValidated userNotValidated = UserNotValidated.builder()
                .userNotValidatedId(userNotValidatedId)
                .password("sarasa")
                .code("123456")
                .build();

        Mockito.when(this.userNotValidatedService.makeValidationCodeForUserNotValidated(any())).thenReturn(userNotValidated);
        Mockito.doNothing().when(this.emailService).sendSimpleMessage(anyString(), anyString(), anyString());
        Mockito.when(this.userNotValidatedService.save(any())).thenReturn(userNotValidated);
        Mockito.when(this.userService.getOptionalByNameAndEmail(anyString(), anyString())).thenReturn(Optional.empty());

        this.mockMvc.perform(post(this.endpointBegin + "/pre-user").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isCreated());
    }

    @Test
    void validatePreUser_thenNotFound() throws Exception {
        this.mockMvc.perform(get(this.endpointBegin + "/pre-user/validate/name/test name/email/test@test.com/code/123456")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void validatePreUser_thenInternalServerError() throws Exception {
        UserNotValidatedId userNotValidatedId = UserNotValidatedId.builder()
                .name("test name")
                .email("test@test.com")
                .build();

        UserNotValidated userNotValidated = UserNotValidated.builder()
                .userNotValidatedId(userNotValidatedId)
                .password("sarasa")
                .code("123456")
                .build();

        Mockito.when(this.userNotValidatedService.findOptionalByIdAndCode(any(), any())).thenReturn(Optional.of(userNotValidated));

        this.mockMvc.perform(get(this.endpointBegin + "/pre-user/validate/name/test name/email/test@test.com/code/123456")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void validatePreUser_theOk() throws Exception {
        UserNotValidatedId userNotValidatedId = UserNotValidatedId.builder()
                .name("test name")
                .email("test@test.com")
                .build();

        UserNotValidated userNotValidated = UserNotValidated.builder()
                .userNotValidatedId(userNotValidatedId)
                .password("sarasa")
                .code("123456")
                .build();

        User userSaved = User.builder().id(1L).name("test name").email("test@test.com").build();

        Mockito.when(this.userNotValidatedService.findOptionalByIdAndCode(any(), any())).thenReturn(Optional.of(userNotValidated));
        Mockito.when(this.userNotValidatedService.deleteById(any())).thenReturn(true);
        Mockito.when(this.userService.save(any())).thenReturn(userSaved);

        this.mockMvc.perform(get(this.endpointBegin + "/pre-user/validate/name/test name/email/test@test.com/code/123456")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
