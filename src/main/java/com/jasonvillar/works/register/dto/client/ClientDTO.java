package com.jasonvillar.works.register.dto.client;

public record ClientDTO(
        long id,
        String name,
        String surname,
        String dni
) {
}
