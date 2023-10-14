package com.jasonvillar.works.register.user_client.port.out;

import com.jasonvillar.works.register.client.port.out.ClientDTO;
import com.jasonvillar.works.register.user.port.out.UserDTO;

public record UserClientDTO(
        long id,
        UserDTO user,
        ClientDTO client
) {
}
