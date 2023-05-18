package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.configtests.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.dto.client.ClientMapper;
import com.jasonvillar.works.register.dto.client.ClientRequest;
import com.jasonvillar.works.register.entities.Client;
import com.jasonvillar.works.register.services.ClientService;
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

@ContextConfiguration(classes = {ClientController.class, ClientMapper.class})
class ClientControllerTest extends ControllerTestTemplate {
    @MockBean
    private ClientService service;

    private final Client entity = Client.builder()
            .name("Name")
            .surname("Surname")
            .dni("11222333")
            .build();

    private final ClientRequest request = new ClientRequest("Name", "Surname", "11222333");

    @BeforeEach
    public void setup() {
        super.setup();
        entity.setId(Long.valueOf(1));
    }

    @Test
    void givenClients_whenGetRequest_thenCheckIfOk() throws Exception {
        Mockito.when(service.getList()).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/clients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getList()).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/clients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenClients_whenGetRequestById_thenCheckIfOk() throws Exception {
        Mockito.when(service.getOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(service.getOptionalById(0)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(this.endpointBegin + "/clients/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(this.endpointBegin + "/clients/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenClients_whenGetRequestByName_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByNameLike("Name")).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/clients/name-like/{name}", "Name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByNameLike("Nonexistent name")).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/clients/name-like/{name}", "Nonexistent name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenClients_whenGetRequestBySurname_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListBySurnameLike("Surname")).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/clients/surname-like/{surname}", "Surname")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListBySurnameLike("Nonexistent surname")).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/clients/surname-like/{name}", "Nonexistent surname")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenClients_whenGetRequestByDni_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByDniLike("11222333")).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/clients/dni-like/{dni}", "11222333")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByDniLike("00000000")).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/clients/dni-like/{dni}", "00000000")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenClients_whenGetRequestByNameAndSurname_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByNameLikeAndSurnameLike("Name", "Surname")).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/clients/name-like/{name}/surname-like/{surname}", "Name", "Surname")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByNameLikeAndSurnameLike("Nonexistent name", "Nonexistent surname")).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/clients/name-like/{name}/surname-like/{surname}", "Nonexistent name", "Nonexistent surname")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNewClient_whenSave_thenCheckIfCreated() throws Exception {
        String requestJson = ow.writeValueAsString(this.request);

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(Client.class))).thenReturn("");
        Mockito.when(service.save(Mockito.any(Client.class))).thenReturn(this.entity);

        this.mockMvc.perform(post(this.endpointBegin + "/clients").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isCreated());

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(Client.class))).thenReturn("dni must be unique");

        this.mockMvc.perform(post(this.endpointBegin + "/clients").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNewClientWithNullAndEmptyProperties_whenBadRequest_thenCheckIsBadRequest() throws Exception {
        String requestJson = ow.writeValueAsString(new ClientRequest("", null, ""));

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(Client.class))).thenReturn("");
        Mockito.when(service.save(Mockito.any(Client.class))).thenReturn(this.entity);

        this.mockMvc.perform(post(this.endpointBegin + "/clients").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }
}
