package com.jasonvillar.works.register.service.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServiceRequest(
        @NotNull
        @NotBlank
        String name
) {
}
