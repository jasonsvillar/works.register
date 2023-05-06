package com.jasonvillar.works.register.services;

import com.jasonvillar.works.register.dto.service.ServiceDTO;
import com.jasonvillar.works.register.dto.service.ServiceMapper;
import com.jasonvillar.works.register.dto.service.ServiceRequest;
import com.jasonvillar.works.register.entities.Service;
import com.jasonvillar.works.register.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public List<Service> getList() {
        return this.serviceRepository.findAll();
    }

    public List<Service> getListByNameLike(String name) {
        return this.serviceRepository.findAllByNameContainingIgnoreCase(name);
    }

    public Service getById(long id) {
        return this.serviceRepository.findServiceById(id);
    }

    public Optional<Service> getOptionalById(long id) {
        return this.serviceRepository.findOptionalById(id);
    }

    public Optional<Service> getOptionalByName(String name) {
        return this.serviceRepository.findOptionalByName(name);
    }

    public boolean isExistId(long id) {
        return this.getOptionalById(id).isPresent();
    }

    public boolean isExistName(String name) {
        return this.getOptionalByName(name).isPresent();
    }

    public String getValidationsMessageWhenCantBeSaved(Service entity) {
        StringBuilder message = new StringBuilder();

        if (this.isExistName(entity.getName())) {
            message.append("name must be unique");
        }

        return message.toString();
    }

    public Service save(Service service) {
        return this.serviceRepository.save(service);
    }
}
