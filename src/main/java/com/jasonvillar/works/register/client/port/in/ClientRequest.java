package com.jasonvillar.works.register.client.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ClientRequest(
        @NotNull
        @NotBlank
        String name,

        @NotNull
        @NotBlank
        String surname,

        @NotNull
        @NotBlank
        @Pattern(regexp="[\\d]{8}", message="should contain 8 digits")
        String dni
) {
}
