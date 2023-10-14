package com.jasonvillar.works.register.client.port.in;

import com.jasonvillar.works.register.client.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientRequestAdapter {
    public Client toEntity(ClientRequest clientRequest) {
        return new Client(clientRequest.name(), clientRequest.surname(), clientRequest.dni());
    }
}
