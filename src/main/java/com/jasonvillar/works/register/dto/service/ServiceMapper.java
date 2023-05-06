package com.jasonvillar.works.register.dto.service;

import com.jasonvillar.works.register.entities.Service;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ServiceMapper implements Function<Service, ServiceDTO> {
    @Override
    public ServiceDTO apply(Service service) {
        return new ServiceDTO(service.getId(), service.getName());
    }

    public Service toEntity(ServiceRequest serviceRequest) {
        return new Service(serviceRequest.name());
    }
}
