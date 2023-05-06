package com.jasonvillar.works.register.dto.client;

import com.jasonvillar.works.register.entities.Client;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ClientMapper implements Function<Client, ClientDTO> {
    @Override
    public ClientDTO apply(Client client) {
        return new ClientDTO(client.getId(), client.getName(), client.getSurname(), client.getDni());
    }

    public Client toEntity(ClientRequest clientRequest) {
        return new Client(clientRequest.name(), clientRequest.surname(), clientRequest.dni());
    }
}
