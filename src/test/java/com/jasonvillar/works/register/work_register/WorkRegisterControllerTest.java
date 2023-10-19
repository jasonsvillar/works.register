package com.jasonvillar.works.register.work_register;

import com.jasonvillar.works.register.authentication.SecurityUserDetailsService;
import com.jasonvillar.works.register.client.Client;
import com.jasonvillar.works.register.configs_for_tests.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.client.port.out.ClientDTOAdapter;
import com.jasonvillar.works.register.service.port.out.ServiceDTOAdapter;
import com.jasonvillar.works.register.service.Service;
import com.jasonvillar.works.register.user.port.out.UserDTOAdapter;
import com.jasonvillar.works.register.work_register.port.in.WorkRegisterRequestAdapter;
import com.jasonvillar.works.register.work_register.port.out.WorkRegisterDTOAdapter;
import com.jasonvillar.works.register.work_register.port.in.WorkRegisterRequest;
import com.jasonvillar.works.register.client.ClientService;
import com.jasonvillar.works.register.service.ServiceService;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserService;
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

@ContextConfiguration(classes = {WorkRegisterController.class, WorkRegisterRequestAdapter.class, WorkRegisterDTOAdapter.class, ClientDTOAdapter.class, ServiceDTOAdapter.class, UserDTOAdapter.class})
class WorkRegisterControllerTest extends ControllerTestTemplate {
    @MockBean
    private WorkRegisterService service;

    @MockBean
    private UserService userService;

    @MockBean
    private ServiceService serviceService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private SecurityUserDetailsService securityUserDetailsService;

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
            .identificationNumber("11222333")
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
            1
    );

    @BeforeEach
    public void setup() {
        super.setup();
        entity.setId(1L);

        userEntity.setId(1L);
        serviceEntity.setId(1L);
        clientEntity.setId(1L);

        entity.setUser(userEntity);
        entity.setService(serviceEntity);
        entity.setClient(clientEntity);
    }

    @Test
    void givenWorkRegisters_whenGetRequest_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByUserId(0)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/works")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenWorkRegisters_whenGetRequestByIdAndUserId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getOptionalByIdAndUserId(1, 0)).thenReturn(Optional.of(entity));
        Mockito.when(service.getOptionalByIdAndUserId(0, 0)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(this.endpointBegin + "/work/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(this.endpointBegin + "/work/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenWorkRegisters_whenGetRequestByClientIdAndUserId_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByUserIdAndClientId(0, 1)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/works/client-id/{clientId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByUserIdAndClientId(0, 0)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/works/client-id/{clientId}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenWorkRegisters_whenGetRequestByTitle_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByTitleLikeAndUserId("Title", 0)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/works/title/{title}", "Title")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByTitleLikeAndUserId("Nonexistent title", 0)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/works/title/{title}", "Nonexistent title")
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

        this.mockMvc.perform(post(this.endpointBegin + "/work").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isCreated());

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(WorkRegister.class))).thenReturn("userId must exist");

        this.mockMvc.perform(post(this.endpointBegin + "/work").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }
}
