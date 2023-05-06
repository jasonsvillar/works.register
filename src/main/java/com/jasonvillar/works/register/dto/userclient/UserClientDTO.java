package com.jasonvillar.works.register.dto.userclient;

import com.jasonvillar.works.register.dto.client.ClientDTO;
import com.jasonvillar.works.register.dto.user.UserDTO;

public record UserClientDTO(
        long id,
        UserDTO user,
        ClientDTO client
) {
}
