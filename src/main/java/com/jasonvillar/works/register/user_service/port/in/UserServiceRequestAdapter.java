package com.jasonvillar.works.register.user_service.port.in;

import com.jasonvillar.works.register.user_service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserServiceRequestAdapter {
    public UserService toEntity(UserServiceRequest userServiceRequest) {
        return new UserService(userServiceRequest.userId(), userServiceRequest.serviceId());
    }
}
