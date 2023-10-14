package com.jasonvillar.works.register.client.port.out;

import com.jasonvillar.works.register.client.Client;
import com.jasonvillar.works.register.client.port.in.ClientRequest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ClientDTOMapper implements Function<Client, ClientDTO> {
    @Override
    public ClientDTO apply(Client client) {
        return new ClientDTO(client.getId(), client.getName(), client.getSurname(), client.getDni());
    }

    public Client toEntity(ClientRequest clientRequest) {
        return new Client(clientRequest.name(), clientRequest.surname(), clientRequest.dni());
    }
}
