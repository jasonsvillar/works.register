package com.jasonvillar.works.register.dto.userservice;

import com.jasonvillar.works.register.dto.service.ServiceDTO;
import com.jasonvillar.works.register.dto.user.UserDTO;

public record UserServiceDTO(
        long id,
        UserDTO user,
        ServiceDTO service
) {
}
