package com.jasonvillar.works.register.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServiceRequest(
        @NotNull
        @NotBlank
        String name
) {
}
