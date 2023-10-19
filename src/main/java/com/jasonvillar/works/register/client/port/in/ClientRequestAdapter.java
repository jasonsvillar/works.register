package com.jasonvillar.works.register.client.port.in;

import com.jasonvillar.works.register.client.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientRequestAdapter {
    public Client toEntity(ClientRequest clientRequest) {
        return Client.builder()
                .name(clientRequest.name())
                .surname(clientRequest.surname())
                .identificationNumber(clientRequest.identificationNumber())
                .build();
    }
}
