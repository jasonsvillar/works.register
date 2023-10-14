package com.jasonvillar.works.register.service.port.out;

import com.jasonvillar.works.register.service.port.in.ServiceRequest;
import com.jasonvillar.works.register.service.Service;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ServiceDTOMapper implements Function<Service, ServiceDTO> {
    @Override
    public ServiceDTO apply(Service service) {
        return new ServiceDTO(service.getId(), service.getName());
    }

    public Service toEntity(ServiceRequest serviceRequest) {
        return new Service(serviceRequest.name());
    }
}
