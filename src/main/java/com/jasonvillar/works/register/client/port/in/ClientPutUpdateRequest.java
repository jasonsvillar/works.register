package com.jasonvillar.works.register.client.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientPutUpdateRequest(
        @NotNull
        long id,
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        String surname,
        @NotNull
        @NotBlank
        String identificationNumber
) {
}
