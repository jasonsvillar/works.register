package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.configtests.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.dto.client.ClientMapper;
import com.jasonvillar.works.register.dto.service.ServiceMapper;
import com.jasonvillar.works.register.dto.user.UserMapper;
import com.jasonvillar.works.register.dto.workregister.WorkRegisterMapper;
import com.jasonvillar.works.register.dto.workregister.WorkRegisterRequest;
import com.jasonvillar.works.register.entities.*;
import com.jasonvillar.works.register.services.ClientService;
import com.jasonvillar.works.register.services.ServiceService;
import com.jasonvillar.works.register.services.WorkRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {WorkRegisterController.class, WorkRegisterMapper.class, ClientMapper.class, ServiceMapper.class, UserMapper.class})
class WorkRegisterControllerTest extends ControllerTestTemplate {
    @MockBean
    private WorkRegisterService service;

    @MockBean
    private com.jasonvillar.works.register.services.UserService userService;

    @MockBean
    private ServiceService serviceService;

    @MockBean
    private ClientService clientService;

    private final User userEntity = User.builder()
            .name("Name")
            .email("test@test.com")
            .build();

    private final Service serviceEntity = Service.builder()
            .name("Name")
            .build();

    private final Client clientEntity = Client.builder()
            .name("Name")
            .surname("Surname")
            .dni("11222333")
            .build();

    private final WorkRegister entity = WorkRegister.builder()
            .title("Title")
            .dateFrom(new Jsr310JpaConverters.LocalDateConverter().convertToEntityAttribute(new Date()))
            .timeFrom(new Jsr310JpaConverters.LocalTimeConverter().convertToEntityAttribute(new Date()))
            .dateTo(new Jsr310JpaConverters.LocalDateConverter().convertToEntityAttribute(new Date()))
            .timeTo(new Jsr310JpaConverters.LocalTimeConverter().convertToEntityAttribute(new Date()))
            .payment(new BigDecimal(1500))
            .userId(1)
            .clientId(1)
            .serviceId(1)
            .build();

    private final WorkRegisterRequest request = new WorkRegisterRequest(
            "Title",
            LocalDate.now(),
            new Jsr310JpaConverters.LocalTimeConverter().convertToEntityAttribute(new Date()),
            new Jsr310JpaConverters.LocalDateConverter().convertToEntityAttribute(new Date()),
            new Jsr310JpaConverters.LocalTimeConverter().convertToEntityAttribute(new Date()),
            new BigDecimal(1500),
            1,
            1,
            1
    );

    @BeforeEach
    public void setup() {
        super.setup();
        entity.setId(Long.valueOf(1));

        userEntity.setId(Long.valueOf(1));
        serviceEntity.setId(Long.valueOf(1));
        clientEntity.setId(Long.valueOf(1));

        entity.setUser(userEntity);
        entity.setService(serviceEntity);
        entity.setClient(clientEntity);
    }

    @Test
    void givenWorkRegisters_whenGetRequest_thenCheckIfOk() throws Exception {
        Mockito.when(service.getList()).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/works-registers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getList()).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/works-registers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenWorkRegisters_whenGetRequestById_thenCheckIfOk() throws Exception {
        Mockito.when(service.getOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(service.getOptionalById(0)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(this.endpointBegin + "/works-registers/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(this.endpointBegin + "/works-registers/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenWorkRegisters_whenGetRequestByUserId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByUserId(1)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/works-registers/user-id/{userId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByUserId(0)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/works-registers/user-id/{userId}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenWorkRegisters_whenGetRequestByClientId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByClientId(1)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/works-registers/client-id/{clientId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByClientId(0)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/works-registers/client-id/{clientId}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenWorkRegisters_whenGetRequestByUserIdAndClientId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByUserIdAndClientId(1, 1)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/works-registers/user-id/{userId}/client-id/{clientId}", 1, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByUserIdAndClientId(0, 0)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/works-registers/user-id/{userId}/client-id/{clientId}", 0, 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenWorkRegisters_whenGetRequestByTitle_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByTitleLike("Title")).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/works-registers/title-like/{title}", "Title")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByTitleLike("Nonexistent title")).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/works-registers/title-like/{title}", "Nonexistent title")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNewUserServices_whenSave_thenCheckIfCreated() throws Exception {
        String requestJson = ow.writeValueAsString(this.request);

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(WorkRegister.class))).thenReturn("");
        Mockito.when(service.save(Mockito.any(WorkRegister.class))).thenReturn(this.entity);
        Mockito.when(userService.getById(1)).thenReturn(userEntity);
        Mockito.when(serviceService.getById(1)).thenReturn(serviceEntity);
        Mockito.when(clientService.getById(1)).thenReturn(clientEntity);

        this.mockMvc.perform(post(this.endpointBegin + "/works-registers").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isCreated());

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(WorkRegister.class))).thenReturn("userId must exist");

        this.mockMvc.perform(post(this.endpointBegin + "/works-registers").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }
}
