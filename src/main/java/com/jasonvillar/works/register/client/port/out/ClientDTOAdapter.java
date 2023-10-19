package com.jasonvillar.works.register.client.port.out;

import com.jasonvillar.works.register.client.Client;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ClientDTOAdapter implements Function<Client, ClientDTO> {
    @Override
    public ClientDTO apply(Client client) {
        return new ClientDTO(client.getId(), client.getName(), client.getSurname(), client.getIdentificationNumber());
    }
}
