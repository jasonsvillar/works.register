package com.jasonvillar.works.register.integration.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jasonvillar.works.register.authentication.port.in.AuthenticationRequest;
import com.jasonvillar.works.register.client.Client;
import com.jasonvillar.works.register.client.port.in.ClientRequest;
import com.jasonvillar.works.register.client.port.out.ClientDTO;
import com.jasonvillar.works.register.integration.IntegrationTestsConfig;
import com.jasonvillar.works.register.user.port.in.UserRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClientIT extends IntegrationTestsConfig {
    @Test
    void Given_2users_When_theySaveClients_Then_canGetOwnedClients() throws Exception {
        TypeReference<List<ClientDTO>> listTypeClientDTO = new TypeReference<>() {};
        String requestJson;
        String responseJson;

        this.saveUser(new UserRequest("User1", "user1@gmail.com", "user1"));

        String adminJWT = this.loginAsAdminAndGetJWT();

        requestJson = ow.writeValueAsString(new ClientRequest("Client1 name - Admin", "Client1 surname", "11.111.111"));
        responseJson = this.doPostRequestWithJWT("/api/v1/client", requestJson, status().isCreated(), adminJWT);
        ClientDTO savedClientOfAdminDTO = mapper.readValue(responseJson, ClientDTO.class);

        responseJson = this.doGetRequestWithJWT("/api/v1/clients", status().isOk(), adminJWT);
        List<ClientDTO> clientOfAdminList = mapper.readValue(responseJson, listTypeClientDTO);
        Assertions.assertThat(clientOfAdminList).hasSize(1);

        responseJson = this.doGetRequestWithJWT("/api/v1/client/" + savedClientOfAdminDTO.id(), status().isOk(), adminJWT);
        ClientDTO getClientOfAdminDTO = mapper.readValue(responseJson, ClientDTO.class);
        Assertions.assertThat(getClientOfAdminDTO).isEqualTo(savedClientOfAdminDTO);

        responseJson = this.doGetRequestWithJWT("/api/v1/clients/name-like/aDmIn", status().isOk(), adminJWT);
        List<ClientDTO> clientOfAdminNameLikeDTO = mapper.readValue(responseJson, listTypeClientDTO);
        Assertions.assertThat(clientOfAdminNameLikeDTO).hasSize(1);

        responseJson = this.doGetRequestWithJWT("/api/v1/clients/surname-like/sUrNaMe", status().isOk(), adminJWT);
        List<ClientDTO> clientOfAdminSurnameLikeDTO = mapper.readValue(responseJson, listTypeClientDTO);
        Assertions.assertThat(clientOfAdminSurnameLikeDTO).hasSize(1);

        //------------User------------//

        String user1JWT = this.loginAndGetJWT(new AuthenticationRequest("User1", "user1"));

        requestJson = ow.writeValueAsString(new ClientRequest("Client1 name - User1", "Client1 surname", "22.222.222"));
        responseJson = this.doPostRequestWithJWT("/api/v1/client", requestJson, status().isCreated(), user1JWT);

        requestJson = ow.writeValueAsString(new ClientRequest("Client2 name - User1", "Client2 surname", "33.333.333"));
        this.doPostRequestWithJWT("/api/v1/client", requestJson, status().isCreated(), user1JWT);
        ClientDTO savedClientOfUserDTO = mapper.readValue(responseJson, ClientDTO.class);

        responseJson = this.doGetRequestWithJWT("/api/v1/client/" + savedClientOfUserDTO.id(), status().isOk(), user1JWT);
        ClientDTO getClientOfUserDTO = mapper.readValue(responseJson, ClientDTO.class);
        Assertions.assertThat(getClientOfUserDTO).isEqualTo(savedClientOfUserDTO);

        responseJson = this.doGetRequestWithJWT("/api/v1/clients", status().isOk(), user1JWT);
        List<ClientDTO> clientOfUser1List = mapper.readValue(responseJson, listTypeClientDTO);
        Assertions.assertThat(clientOfUser1List).hasSize(2);

        responseJson = this.doGetRequestWithJWT("/api/v1/clients/name-like/uSeR1", status().isOk(), user1JWT);
        List<ClientDTO> clientOfUser1NameLikeDTO = mapper.readValue(responseJson, listTypeClientDTO);
        Assertions.assertThat(clientOfUser1NameLikeDTO).hasSize(2);
    }
}
