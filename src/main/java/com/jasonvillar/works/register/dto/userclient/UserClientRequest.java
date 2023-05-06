package com.jasonvillar.works.register.dto.userclient;

import jakarta.validation.constraints.NotNull;

public record UserClientRequest(
        @NotNull
        long userId,
        @NotNull
        long clientId
) {
}
