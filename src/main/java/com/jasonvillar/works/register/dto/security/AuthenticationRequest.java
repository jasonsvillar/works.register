package com.jasonvillar.works.register.dto.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest(
        @NotNull
        @NotBlank
        String userName,
        @NotNull
        @NotBlank
        String password
) {
}
