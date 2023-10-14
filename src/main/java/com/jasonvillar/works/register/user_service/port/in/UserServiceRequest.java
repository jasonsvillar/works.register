package com.jasonvillar.works.register.user_service.port.in;

import jakarta.validation.constraints.NotNull;

public record UserServiceRequest(
        @NotNull
        long userId,

        @NotNull
        long serviceId
) {
}
