package com.jasonvillar.works.register.controllers;

import com.jasonvillar.works.register.config.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.dto.service.ServiceMapper;
import com.jasonvillar.works.register.dto.service.ServiceRequest;
import com.jasonvillar.works.register.entities.Service;
import com.jasonvillar.works.register.services.ServiceService;
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

@ContextConfiguration(classes = {ServiceController.class, ServiceMapper.class})
class ServiceControllerTest extends ControllerTestTemplate {
    @MockBean
    private ServiceService service;

    private final Service entity = Service.builder()
            .name("Name")
            .build();

    private final ServiceRequest request = new ServiceRequest("Name");

    @BeforeEach
    public void setup() {
        super.setup();
        entity.setId(Long.valueOf(1));
    }

    @Test
    void givenServices_whenGetRequest_thenCheckIfOk() throws Exception {
        Mockito.when(service.getList()).thenReturn(List.of(entity));

        this.mockMvc.perform(get("/services")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getList()).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/services")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenServices_whenGetRequestById_thenCheckIfOk() throws Exception {
        Mockito.when(service.getOptionalById(1)).thenReturn(Optional.of(entity));
        Mockito.when(service.getOptionalById(0)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/services/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/services/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenServices_whenGetRequestByName_thenCheckIfOk() throws Exception {
        Mockito.when(service.getListByNameLike("Name")).thenReturn(List.of(entity));

        this.mockMvc.perform(get("/services/name-like/{name}", "Name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.when(service.getListByNameLike("Nonexistent name")).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/services/name-like/{name}", "Nonexistent name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNewService_whenSave_thenCheckIfCreated() throws Exception {
        String requestJson = ow.writeValueAsString(this.request);

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(Service.class))).thenReturn("");
        Mockito.when(service.save(Mockito.any(Service.class))).thenReturn(this.entity);

        this.mockMvc.perform(post("/services").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(Mockito.any(Service.class))).thenReturn("name must be unique");

        this.mockMvc.perform(post("/services").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}
