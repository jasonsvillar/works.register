package com.jasonvillar.works.register.user.user_not_validated.port.in;

public record ValidateUserRequest(
    String name,
    String email,
    String code
) {
}
