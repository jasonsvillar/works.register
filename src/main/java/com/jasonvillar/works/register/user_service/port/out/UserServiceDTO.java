package com.jasonvillar.works.register.user_service.port.out;

import com.jasonvillar.works.register.service.port.out.ServiceDTO;
import com.jasonvillar.works.register.user.port.out.UserDTO;

public record UserServiceDTO(long id, UserDTO userDTO, ServiceDTO serviceDTO) {
}
