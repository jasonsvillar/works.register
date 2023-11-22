package com.jasonvillar.works.register.user.user_not_validated.port.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserNotValidatedRequest(
        @NotNull
        @NotBlank
        String name,

        @NotNull
        @NotBlank
        @Email
        String email,

        @NotNull
        @NotBlank
        String password,

        @NotNull
        @NotBlank
        String frontendUrlForValidating
) {
}