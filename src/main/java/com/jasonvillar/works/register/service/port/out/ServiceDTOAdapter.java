package com.jasonvillar.works.register.service.port.out;

import com.jasonvillar.works.register.service.Service;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ServiceDTOAdapter implements Function<Service, ServiceDTO> {
    @Override
    public ServiceDTO apply(Service service) {
        return new ServiceDTO(service.getId(), service.getName());
    }
}
