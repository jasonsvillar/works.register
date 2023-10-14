package com.jasonvillar.works.register.client.port.out;

public record ClientDTO(
        long id,
        String name,
        String surname,
        String dni
) {
}
