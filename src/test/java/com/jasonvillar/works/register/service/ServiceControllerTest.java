package com.jasonvillar.works.register.service;

import com.jasonvillar.works.register.authentication.SecurityUserDetailsService;
import com.jasonvillar.works.register.configs_for_tests.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.service.port.in.ServiceRequestAdapter;
import com.jasonvillar.works.register.service.port.out.ServiceDTOAdapter;
import com.jasonvillar.works.register.service.port.in.ServiceRequest;
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

@ContextConfiguration(classes = {ServiceController.class, ServiceRequestAdapter.class, ServiceDTOAdapter.class})
class ServiceControllerTest extends ControllerTestTemplate {
    @MockBean
    private ServiceService service;

    private final Service entity = Service.builder()
            .name("Name")
            .build();

    private final ServiceRequest request = new ServiceRequest("Name");

    @MockBean
    private SecurityUserDetailsService securityUserDetailsService;

    @BeforeEach
    public void setup() {
        super.setup();
        entity.setId(1L);
    }

    @Test
    void givenServices_whenGetRequest_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByUserId(0)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/services")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenServices_whenGetRequestById_thenCheckIfOk() throws Exception {
        Mockito.when(service.getOptionalByIdAndUserId(1, 0)).thenReturn(Optional.of(entity));
        Mockito.when(service.getOptionalByIdAndUserId(0, 0)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(this.endpointBegin + "/service/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(this.endpointBegin + "/service/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenServices_whenGetRequestByName_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByNameLikeAndUserId("Name", 0)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/services/name-like/{name}", "Name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByNameLikeAndUserId("Nonexistent name", 0)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get(this.endpointBegin + "/services/name-like/{name}", "Nonexistent name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNewService_whenSave_thenCheckIfCreated() throws Exception {
        String requestJson = ow.writeValueAsString(this.request);

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(Service.class))).thenReturn("");
        Mockito.when(service.saveWithUser(Mockito.any(Service.class), eq(0L))).thenReturn(this.entity);

        this.mockMvc.perform(post(this.endpointBegin + "/service").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isCreated());

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(Service.class))).thenReturn("name must be unique");

        this.mockMvc.perform(post(this.endpointBegin + "/service").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }
}
