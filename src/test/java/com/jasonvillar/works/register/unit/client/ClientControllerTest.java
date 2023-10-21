package com.jasonvillar.works.register.unit.client;

import com.jasonvillar.works.register.authentication.SecurityUserDetailsService;
import com.jasonvillar.works.register.client.Client;
import com.jasonvillar.works.register.client.ClientController;
import com.jasonvillar.works.register.client.ClientService;
import com.jasonvillar.works.register.client.port.in.ClientRequestAdapter;
import com.jasonvillar.works.register.unit.configs_for_tests.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.client.port.out.ClientDTOAdapter;
import com.jasonvillar.works.register.client.port.in.ClientRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {ClientController.class, ClientRequestAdapter.class, ClientDTOAdapter.class})
class ClientControllerTest extends ControllerTestTemplate {
    @MockBean
    private ClientService service;

    @MockBean
    private SecurityUserDetailsService securityUserDetailsService;

    private final Client entity = Client.builder()
            .name("Name")
            .surname("Surname")
            .identificationNumber("11222333")
            .build();

    private final ClientRequest request = new ClientRequest("Name", "Surname", "11222333");

    @BeforeEach
    public void setup() {
        super.setup();
        entity.setId(1L);
    }

    @Test
    void givenClients_whenGetRequestByUserId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByUserId(0)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/clients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenClients_whenGetRequestByIdAndUserId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getOptionalByIdAndUserId(1, 0)).thenReturn(Optional.of(entity));
        Mockito.when(service.getOptionalByIdAndUserId(0, 0)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(this.endpointBegin + "/client/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(this.endpointBegin + "/client/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenClients_whenGetRequestByNameAndUserId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByNameLikeAndUserId("Name", 0)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/clients/name-like/{name}", "Name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByNameLikeAndUserId("Nonexistent name", 0)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/clients/name-like/{name}", "Nonexistent name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenClients_whenGetRequestBySurnameAndUserId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListBySurnameLikeAndUserId("Surname", 0)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/clients/surname-like/{surname}", "Surname")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListBySurnameLikeAndUserId("Nonexistent surname", 0)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/clients/surname-like/{name}", "Nonexistent surname")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenClients_whenGetRequestByIdentificationNumberAndUserId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByIdentificationNumberLikeAndUserId("11222333", 0)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/clients/dni-like/{dni}", "11222333")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByIdentificationNumberLikeAndUserId("00000000", 0)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/clients/dni-like/{dni}", "00000000")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenClients_whenGetRequestByNameAndSurnameAndUserId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByNameLikeAndSurnameLikeAndUserId("Name", "Surname", 0)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/clients/name-like/{name}/surname-like/{surname}", "Name", "Surname")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByNameLikeAndSurnameLikeAndUserId("Nonexistent name", "Nonexistent surname", 0)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/clients/name-like/{name}/surname-like/{surname}", "Nonexistent name", "Nonexistent surname")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNewClientWithUserId_whenSave_thenCheckIfCreated() throws Exception {
        String requestJson = ow.writeValueAsString(this.request);

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(Client.class), eq(0L))).thenReturn("");
        Mockito.when(service.save(Mockito.any(Client.class))).thenReturn(this.entity);

        this.mockMvc.perform(post(this.endpointBegin + "/client").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isCreated());

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(Client.class), eq(0L))).thenReturn("dni must be unique");

        this.mockMvc.perform(post(this.endpointBegin + "/client").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNewClientWithNullAndEmptyProperties_whenBadRequest_thenCheckIsBadRequest() throws Exception {
        String requestJson = ow.writeValueAsString(new ClientRequest("", null, ""));

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(Client.class), eq(0))).thenReturn("");
        Mockito.when(service.save(Mockito.any(Client.class))).thenReturn(this.entity);

        this.mockMvc.perform(post(this.endpointBegin + "/client").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }
}
