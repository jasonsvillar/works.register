package com.jasonvillar.works.register.integration.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jasonvillar.works.register.authentication.port.in.AuthenticationRequest;
import com.jasonvillar.works.register.integration.IntegrationTestsConfig;
import com.jasonvillar.works.register.service.port.in.ServiceRequest;
import com.jasonvillar.works.register.service.port.out.ServiceDTO;
import com.jasonvillar.works.register.user.port.in.UserRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ServiceIT extends IntegrationTestsConfig {
    @Test
    void Given_2users_When_theySaveServices_Then_canGetOwnedServices() throws Exception {
        TypeReference<List<ServiceDTO>> listTypeServiceDTO = new TypeReference<>() {};
        String requestJson;
        String responseJson;

        String adminJWT = this.loginAsAdminAndGetJWT();

        this.saveUser(new UserRequest("User1 for service IT", "user1@serviceit.com", "sarasa"), adminJWT);

        requestJson = ow.writeValueAsString(new ServiceRequest("Service1 of admin"));
        responseJson = this.doPostRequestWithJWT("/api/v1/service", requestJson, status().isCreated(), adminJWT);
        ServiceDTO savedServiceOfAdminDTO = mapper.readValue(responseJson, ServiceDTO.class);

        responseJson = this.doGetRequestWithJWT("/api/v1/services/page/1/rows/10", status().isOk(), adminJWT);
        List<ServiceDTO> serviceOfAdminList = mapper.readValue(responseJson, listTypeServiceDTO);
        Assertions.assertThat(serviceOfAdminList).hasSize(1);

        responseJson = this.doGetRequestWithJWT("/api/v1/service/" + savedServiceOfAdminDTO.id(), status().isOk(), adminJWT);
        ServiceDTO getServiceOfAdminDTO = mapper.readValue(responseJson, ServiceDTO.class);
        Assertions.assertThat(getServiceOfAdminDTO).isEqualTo(savedServiceOfAdminDTO);

        responseJson = this.doGetRequestWithJWT("/api/v1/services/name-like/AdMiN", status().isOk(), adminJWT);
        List<ServiceDTO> serviceOfAdminNameLikeDTO = mapper.readValue(responseJson, listTypeServiceDTO);
        Assertions.assertThat(serviceOfAdminNameLikeDTO).hasSize(1);

        //------------User------------//

        String user1JWT = this.loginAndGetJWT(new AuthenticationRequest("User1 for service IT", "sarasa"));

        requestJson = ow.writeValueAsString(new ServiceRequest("Service1 of user1"));
        responseJson = this.doPostRequestWithJWT("/api/v1/service", requestJson, status().isCreated(), user1JWT);

        requestJson = ow.writeValueAsString(new ServiceRequest("Service2 of user1"));
        this.doPostRequestWithJWT("/api/v1/service", requestJson, status().isCreated(), user1JWT);
        ServiceDTO savedServiceOfUserDTO = mapper.readValue(responseJson, ServiceDTO.class);

        responseJson = this.doGetRequestWithJWT("/api/v1/service/" + savedServiceOfUserDTO.id(), status().isOk(), user1JWT);
        ServiceDTO getServiceOfUserDTO = mapper.readValue(responseJson, ServiceDTO.class);
        Assertions.assertThat(getServiceOfUserDTO).isEqualTo(savedServiceOfUserDTO);

        responseJson = this.doGetRequestWithJWT("/api/v1/services/page/1/rows/10", status().isOk(), user1JWT);
        List<ServiceDTO> serviceOfUser1List = mapper.readValue(responseJson, listTypeServiceDTO);
        Assertions.assertThat(serviceOfUser1List).hasSize(2);

        responseJson = this.doGetRequestWithJWT("/api/v1/services/name-like/uSeR1", status().isOk(), user1JWT);
        List<ServiceDTO> serviceOfUser1NameLikeDTO = mapper.readValue(responseJson, listTypeServiceDTO);
        Assertions.assertThat(serviceOfUser1NameLikeDTO).hasSize(2);
    }
}
