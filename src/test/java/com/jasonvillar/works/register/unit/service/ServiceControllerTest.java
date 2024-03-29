package com.jasonvillar.works.register.unit.service;

import com.jasonvillar.works.register.authentication.SecurityUserDetailsService;
import com.jasonvillar.works.register.service.port.in.ServicePutUpdateRequest;
import com.jasonvillar.works.register.unit.configs_for_tests.controllers.ControllerTestTemplate;
import com.jasonvillar.works.register.service.Service;
import com.jasonvillar.works.register.service.ServiceController;
import com.jasonvillar.works.register.service.ServiceService;
import com.jasonvillar.works.register.service.port.in.ServiceRequestAdapter;
import com.jasonvillar.works.register.service.port.out.ServiceDTOAdapter;
import com.jasonvillar.works.register.service.port.in.ServiceRequest;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.port.out.UserDTOAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {ServiceController.class, ServiceRequestAdapter.class, ServiceDTOAdapter.class, UserDTOAdapter.class})
class ServiceControllerTest extends ControllerTestTemplate {
    @MockBean
    private ServiceService service;

    @MockBean
    private com.jasonvillar.works.register.user.UserService userService;

    private final User userEntity = User.builder()
            .id(1L)
            .name("Name")
            .validated(true)
            .build();

    private final Service entity = Service.builder()
            .name("Name")
            .user(this.userEntity)
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
        Mockito.when(service.getListBySpecificationAndPage(any(), eq(0), eq(10))).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/services/page/1/rows/10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenServices_whenGetRequestAllSystemServices_thenCheckIfOk() throws Exception {
        Mockito.when(service.getList(0, 10)).thenReturn(List.of(entity));

        this.mockMvc.perform(get(this.endpointBegin + "/services/all/page/1/rows/10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenServices_whenGetRowCount_thenCheckIfOk() throws Exception {
        Mockito.when(service.getRowCountBySpecification(any())).thenReturn(1L);

        this.mockMvc.perform(get(this.endpointBegin + "/services/row-count")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenServices_whenGetAllRowCount_thenCheckIfOk() throws Exception {
        Mockito.when(service.getRowCount()).thenReturn(1L);

        this.mockMvc.perform(get(this.endpointBegin + "/services/all/row-count")
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

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(any(Service.class))).thenReturn("");
        Mockito.when(userService.getById(0)).thenReturn(userEntity);
        Mockito.when(service.save(any(Service.class))).thenReturn(this.entity);

        this.mockMvc.perform(post(this.endpointBegin + "/service").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isCreated());

        Mockito.when(service.getValidationsMessageWhenCantBeSaved(any(Service.class))).thenReturn("name must be unique");

        this.mockMvc.perform(post(this.endpointBegin + "/service").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNewService_whenDeleteService_thenCheckStatusIfOk() throws Exception {
        Mockito.when(service.deleteByServiceIdAndUserId(1L, 0L)).thenReturn(true);

        this.mockMvc.perform(delete(this.endpointBegin + "/service/1").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().isOk());

        Mockito.when(service.deleteByServiceIdAndUserId(999L, 0L)).thenReturn(false);

        this.mockMvc.perform(delete(this.endpointBegin + "/service/999").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNewService_whenPutUpdateService_thenCheckStatusIfOk() throws Exception {
        Mockito.when(service.getOptionalByIdAndUserId(1L, 0L)).thenReturn(Optional.of(entity));
        Mockito.when(userService.getById(0)).thenReturn(userEntity);
        Mockito.when(service.save(any())).thenReturn(entity);

        ServicePutUpdateRequest servicePutUpdateRequest = new ServicePutUpdateRequest(1L, "Name");
        String requestJson = ow.writeValueAsString(servicePutUpdateRequest);

        this.mockMvc.perform(put(this.endpointBegin + "/service").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isOk());


        Mockito.when(service.getOptionalByIdAndUserId(999L, 0L)).thenReturn(Optional.empty());

        servicePutUpdateRequest = new ServicePutUpdateRequest(999L, "Name");
        requestJson = ow.writeValueAsString(servicePutUpdateRequest);

        this.mockMvc.perform(put(this.endpointBegin + "/service").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isNotFound());
    }
}
