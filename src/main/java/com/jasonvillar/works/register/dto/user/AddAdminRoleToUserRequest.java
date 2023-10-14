package com.jasonvillar.works.register.dto.user;

import jakarta.validation.constraints.NotNull;

public record AddAdminRoleToUserRequest(
        @NotNull
        Long id
) {
}
