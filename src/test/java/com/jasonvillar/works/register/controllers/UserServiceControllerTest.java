package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.configtests.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.dto.service.ServiceMapper;
import com.jasonvillar.works.register.dto.user.UserMapper;
import com.jasonvillar.works.register.dto.userclient.UserClientRequest;
import com.jasonvillar.works.register.dto.userservice.UserServiceMapper;
import com.jasonvillar.works.register.entities.Service;
import com.jasonvillar.works.register.entities.User;
import com.jasonvillar.works.register.entities.UserService;
import com.jasonvillar.works.register.services.*;
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

@ContextConfiguration(classes = {UserServiceController.class, UserServiceMapper.class, UserMapper.class, ServiceMapper.class})
class UserServiceControllerTest extends ControllerTestTemplate {
    @MockBean
    private UserServiceService service;

    @MockBean
    private com.jasonvillar.works.register.services.UserService userService;

    @MockBean
    private ServiceService serviceService;

    private final User userEntity = User.builder()
            .name("Name")
            .email("test@test.com")
            .build();

    private final Service serviceEntity = Service.builder()
            .name("Name")
            .build();

    private final UserService entity = UserService.builder()
            .userId(1)
            .serviceId(1)
            .build();

    private final UserClientRequest request = new UserClientRequest(1, 1);

    @BeforeEach
    public void setup() {
        super.setup();
        entity.setId(Long.valueOf(1));

        userEntity.setId(Long.valueOf(1));
        serviceEntity.setId(Long.valueOf(1));

        entity.setUser(userEntity);
        entity.setService(serviceEntity);
    }

    @Test
    void givenUserServices_whenGetRequest_thenCheckIfOk() throws Exception {
        Mockito.when(service.getList()).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/users-services")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getList()).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/users-services")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenUserServices_whenGetRequestById_thenCheckIfOk() throws Exception {
        Mockito.when(service.getOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(service.getOptionalById(0)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(this.endpointBegin + "/users-services/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(this.endpointBegin + "/users-services/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUserServices_whenGetRequestByUserId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByUserId(1)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/users-services/user-id/{userId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByUserId(0)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/users-services/user-id/{userId}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenNewUserServices_whenSave_thenCheckIfCreated() throws Exception {
        String requestJson = ow.writeValueAsString(this.request);

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(UserService.class))).thenReturn("");
        Mockito.when(service.save(Mockito.any(UserService.class))).thenReturn(this.entity);
        Mockito.when(userService.getById(1)).thenReturn(userEntity);
        Mockito.when(serviceService.getById(1)).thenReturn(serviceEntity);

        this.mockMvc.perform(post(this.endpointBegin + "/users-services").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isCreated());

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(UserService.class))).thenReturn("userId must exist");

        this.mockMvc.perform(post(this.endpointBegin + "/users-services").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }
}
