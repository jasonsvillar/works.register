package com.jasonvillar.works.register.unit.user;

import com.jasonvillar.works.register.unit.configs_for_tests.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserController;
import com.jasonvillar.works.register.user.UserService;
import com.jasonvillar.works.register.user.port.in.UserRequestAdapter;
import com.jasonvillar.works.register.user.port.out.UserDTOAdapter;
import com.jasonvillar.works.register.user.port.in.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {UserController.class, UserRequestAdapter.class, UserDTOAdapter.class})
class UserControllerTest extends ControllerTestTemplate {
    @MockBean
    private UserService service;

    private final User entity = User.builder()
            .name("Name")
            .email("test@test.com")
            .password("top-secret-encrypted-password")
            .build();

    private final UserRequest request = new UserRequest("Name", "test@test.com", "pass");

    @BeforeEach
    public void setup() {
        super.setup();
        entity.setId(1L);
    }

    @Test
    void givenUsers_whenGetRequest_thenCheckIfOk() throws Exception {
        Mockito.when(service.getList()).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getList()).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenUsers_whenGetRequestById_thenCheckIfOk() throws Exception {
        Mockito.when(service.getOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(service.getOptionalById(0)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(this.endpointBegin + "/users/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(this.endpointBegin + "/users/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUsers_whenGetRequestByName_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByNameLike("Name")).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/users/name-like/{name}", "Name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByNameLike("Nonexistent name")).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/users/name-like/{name}", "Nonexistent name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUsers_whenGetRequestByNameAndEmail_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByNameLikeAndEmailLike("Name", "test@test.com")).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/users/name-like/{name}/email-like/{email}", "Name", "test@test.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByNameLikeAndEmailLike("Nonexistent name", "Nonexistent email")).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/users/name-like/{name}/email-like/{email}", "Nonexistent name", "Nonexistent email")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUsers_whenGetRequestByEmail_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByEmailLike("test@test.com")).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/users/email-like/{email}", "test@test.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByEmailLike("Nonexistent name")).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/users/email-like/{email}", "Nonexistent email")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNewUser_whenSave_thenCheckIfCreated() throws Exception {
        String requestJson = ow.writeValueAsString(this.request);

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(User.class))).thenReturn("");
        Mockito.when(service.save(Mockito.any(User.class))).thenReturn(this.entity);

        this.mockMvc.perform(post(this.endpointBegin + "/users").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isCreated());

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(User.class))).thenReturn("name must be unique");

        this.mockMvc.perform(post(this.endpointBegin + "/users").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }
}
