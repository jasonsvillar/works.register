package com.jasonvillar.works.register.user.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordRequest(
        @NotBlank
        @NotNull
        String oldPassword,

        @NotBlank
        @NotNull
        String newPassword,

        @NotBlank
        @NotNull
        String repeatNewPassword
) {
}
