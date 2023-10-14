package com.jasonvillar.works.register.user_client.port.in;

import jakarta.validation.constraints.NotNull;

public record UserClientRequest(
        @NotNull
        long userId,
        @NotNull
        long clientId
) {
}
