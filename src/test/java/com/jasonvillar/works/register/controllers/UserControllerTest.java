package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.config.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.dto.user.UserMapper;
import com.jasonvillar.works.register.dto.user.UserRequest;
import com.jasonvillar.works.register.entities.User;
import com.jasonvillar.works.register.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {UserController.class, UserMapper.class})
class UserControllerTest extends ControllerTestTemplate {
    @MockBean
    private UserService service;

    private final User entity = User.builder()
            .name("Name")
            .email("test@test.com")
            .build();

    private final UserRequest request = new UserRequest("Name", "test@test.com");

    @BeforeEach
    public void setup() {
        super.setup();
        entity.setId(Long.valueOf(1));
    }

    @Test
    void givenUsers_whenGetRequest_thenCheckIfOk() throws Exception {
        Mockito.when(service.getList()).thenReturn(List.of(entity));

        this.mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getList()).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenUsers_whenGetRequestById_thenCheckIfOk() throws Exception {
        Mockito.when(service.getOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(service.getOptionalById(0)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/users/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/users/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUsers_whenGetRequestByName_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByNameLike("Name")).thenReturn(List.of(entity));

        this.mockMvc.perform(get("/users/name-like/{name}", "Name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByNameLike("Nonexistent name")).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/users/name-like/{name}", "Nonexistent name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUsers_whenGetRequestByNameAndEmail_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByNameLikeAndEmailLike("Name", "test@test.com")).thenReturn(List.of(entity));

        this.mockMvc.perform(get("/users/name-like/{name}/email-like/{email}", "Name", "test@test.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByNameLikeAndEmailLike("Nonexistent name", "Nonexistent email")).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/users/name-like/{name}/email-like/{email}", "Nonexistent name", "Nonexistent email")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUsers_whenGetRequestByEmail_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByEmailLike("test@test.com")).thenReturn(List.of(entity));

        this.mockMvc.perform(get("/users/email-like/{email}", "test@test.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByEmailLike("Nonexistent name")).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/users/email-like/{email}", "Nonexistent email")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNewUser_whenSave_thenCheckIfCreated() throws Exception {
        String requestJson = ow.writeValueAsString(this.request);

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(User.class))).thenReturn("");
        Mockito.when(service.save(Mockito.any(User.class))).thenReturn(this.entity);

        this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(User.class))).thenReturn("name must be unique");

        this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}
