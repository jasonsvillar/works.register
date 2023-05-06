package com.jasonvillar.works.register.dto.userservice;

import jakarta.validation.constraints.NotNull;

public record UserServiceRequest(
        @NotNull
        long userId,

        @NotNull
        long serviceId
) {
}
