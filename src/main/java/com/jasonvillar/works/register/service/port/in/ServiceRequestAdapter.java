package com.jasonvillar.works.register.service.port.in;

import com.jasonvillar.works.register.service.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceRequestAdapter {
    public Service toEntity(ServiceRequest serviceRequest) {
        return new Service(serviceRequest.name());
    }
}
