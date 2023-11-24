package com.jasonvillar.works.register.service.port.in;

import com.jasonvillar.works.register.service.Service;
import com.jasonvillar.works.register.user.User;
import org.springframework.stereotype.Component;

@Component
public class ServiceRequestAdapter {
    public Service toEntity(ServiceRequest serviceRequest, User user) {
        return Service.builder()
                .name(serviceRequest.name())
                .user(user)
                .build();
    }
}
