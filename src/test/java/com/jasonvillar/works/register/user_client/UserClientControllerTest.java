package com.jasonvillar.works.register.user_client;

import com.jasonvillar.works.register.configs_for_tests.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.client.port.out.ClientDTOAdapter;
import com.jasonvillar.works.register.user.port.out.UserDTOAdapter;
import com.jasonvillar.works.register.user_client.port.in.UserClientRequestAdapter;
import com.jasonvillar.works.register.user_client.port.out.UserClientDTOAdapter;
import com.jasonvillar.works.register.user_client.port.in.UserClientRequest;
import com.jasonvillar.works.register.client.Client;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.client.ClientService;
import com.jasonvillar.works.register.user.UserService;
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

@ContextConfiguration(classes = {UserClientController.class, UserClientRequestAdapter.class, UserClientDTOAdapter.class, UserDTOAdapter.class, ClientDTOAdapter.class})
class UserClientControllerTest extends ControllerTestTemplate {
    @MockBean
    private UserClientService service;

    @MockBean
    private UserService userService;

    @MockBean
    private ClientService clientService;

    private final User userEntity = User.builder()
            .name("Name")
            .email("test@test.com")
            .build();

    private final Client clientEntity = Client.builder()
            .name("Name")
            .surname("Surname")
            .dni("11222333")
            .build();

    private final UserClient entity = UserClient.builder()
            .userId(1)
            .clientId(1)
            .build();

    private final UserClientRequest request = new UserClientRequest(1, 1);

    @BeforeEach
    public void setup() {
        super.setup();
        entity.setId(1L);

        userEntity.setId(1L);
        clientEntity.setId(1L);

        entity.setUser(userEntity);
        entity.setClient(clientEntity);
    }

    @Test
    void givenUserClients_whenGetRequest_thenCheckIfOk() throws Exception {
        Mockito.when(service.getList()).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/users-clients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getList()).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/users-clients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenUserClients_whenGetRequestById_thenCheckIfOk() throws Exception {
        Mockito.when(service.getOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(service.getOptionalById(0)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(this.endpointBegin + "/users-clients/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(this.endpointBegin + "/users-clients/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUserClients_whenGetRequestByUserId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByUserId(1)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/users-clients/user-id/{userId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByUserId(0)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/users-clients/user-id/{userId}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenNewUserClient_whenSave_thenCheckIfCreated() throws Exception {
        String requestJson = ow.writeValueAsString(this.request);

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(UserClient.class))).thenReturn("");
        Mockito.when(service.save(Mockito.any(UserClient.class))).thenReturn(this.entity);
        Mockito.when(userService.getById(1)).thenReturn(userEntity);
        Mockito.when(clientService.getById(1)).thenReturn(clientEntity);

        this.mockMvc.perform(post(this.endpointBegin + "/users-clients").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isCreated());

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(UserClient.class))).thenReturn("userId must exist");

        this.mockMvc.perform(post(this.endpointBegin + "/users-clients").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }
}
