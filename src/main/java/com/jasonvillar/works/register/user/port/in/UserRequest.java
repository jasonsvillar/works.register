package com.jasonvillar.works.register.user.port.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotNull
        @NotBlank
        String name,

        @NotNull
        @NotBlank
        @Email
        String email,

        @NotNull
        @NotBlank
        String password
) {
}
