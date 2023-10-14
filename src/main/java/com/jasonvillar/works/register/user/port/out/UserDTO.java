package com.jasonvillar.works.register.user.port.out;

public record UserDTO(
        long id,
        String name,
        String email
) {
}
