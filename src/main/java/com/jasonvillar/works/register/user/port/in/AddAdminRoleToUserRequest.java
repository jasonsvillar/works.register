package com.jasonvillar.works.register.user.port.in;

import jakarta.validation.constraints.NotNull;

public record AddAdminRoleToUserRequest(
        @NotNull
        Long id
) {
}
