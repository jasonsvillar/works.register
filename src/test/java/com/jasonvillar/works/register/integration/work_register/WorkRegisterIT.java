package com.jasonvillar.works.register.integration.work_register;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jasonvillar.works.register.client.port.in.ClientRequest;
import com.jasonvillar.works.register.client.port.out.ClientDTO;
import com.jasonvillar.works.register.integration.IntegrationTestsConfig;
import com.jasonvillar.works.register.service.port.in.ServiceRequest;
import com.jasonvillar.works.register.service.port.out.ServiceDTO;
import com.jasonvillar.works.register.user.port.in.UserRequest;
import com.jasonvillar.works.register.work_register.port.in.WorkRegisterRequest;
import com.jasonvillar.works.register.work_register.port.out.WorkRegisterDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WorkRegisterIT extends IntegrationTestsConfig {
    @Test
    void Given_userAdmin_When_SaveWorks_Then_canGetOwnedWorks() throws Exception {
        TypeReference<List<WorkRegisterDTO>> listTypeWorkDTO = new TypeReference<>() {};
        String requestJson;
        String responseJson;

        TypeReference<List<ServiceDTO>> listTypeServiceDTO = new TypeReference<>() {};
        TypeReference<List<ServiceDTO>> listTypeClientDTO = new TypeReference<>() {};

        this.saveUser(new UserRequest("User1", "user1@gmail.com", "user1"));

        String adminJWT = this.loginAsAdminAndGetJWT();

        requestJson = ow.writeValueAsString(new ServiceRequest("Service1 - Admin"));
        responseJson = this.doPostRequestWithJWT("/api/v1/service", requestJson, status().isCreated(), adminJWT);
        ServiceDTO savedServiceOfAdminDTO = mapper.readValue(responseJson, ServiceDTO.class);

        requestJson = ow.writeValueAsString(new ClientRequest("Client1 Name - Admin", "Client1 Surname - Admin", "11.111.111"));
        responseJson = this.doPostRequestWithJWT("/api/v1/client", requestJson, status().isCreated(), adminJWT);
        ClientDTO savedClientOfAdminDTO = mapper.readValue(responseJson, ClientDTO.class);

        requestJson = ow.writeValueAsString(
                new WorkRegisterRequest(
                        "Work1 - Admin",
                        LocalDate.now().atTime(0,0,0).toLocalDate(),
                        LocalTime.now().withNano(0),
                        LocalDate.now().atTime(0,0,0).toLocalDate(),
                        LocalTime.now().withNano(0),
                        BigDecimal.valueOf(100),
                        savedServiceOfAdminDTO.id(),
                        savedClientOfAdminDTO.id()
                )
        );
        responseJson = this.doPostRequestWithJWT("/api/v1/work", requestJson, status().isCreated(), adminJWT);
        WorkRegisterDTO savedWorkOfAdminDTO = mapper.readValue(responseJson, WorkRegisterDTO.class);

        responseJson = this.doGetRequestWithJWT("/api/v1/works", status().isOk(), adminJWT);
        List<WorkRegisterDTO> workOfAdminList = mapper.readValue(responseJson, listTypeWorkDTO);
        Assertions.assertThat(workOfAdminList).hasSize(1);

        responseJson = this.doGetRequestWithJWT("/api/v1/work/" + savedWorkOfAdminDTO.id(), status().isOk(), adminJWT);
        WorkRegisterDTO getWorkOfAdminDTO = mapper.readValue(responseJson, WorkRegisterDTO.class);
        Assertions.assertThat(getWorkOfAdminDTO).isEqualTo(savedWorkOfAdminDTO);

        responseJson = this.doGetRequestWithJWT("/api/v1/works/title/aDmIn", status().isOk(), adminJWT);
        List<WorkRegisterDTO> workOfAdminNameLikeDTO = mapper.readValue(responseJson, listTypeWorkDTO);
        Assertions.assertThat(workOfAdminNameLikeDTO).hasSize(1);
    }
}
