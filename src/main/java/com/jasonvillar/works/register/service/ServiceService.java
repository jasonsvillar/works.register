package com.jasonvillar.works.register.service;

import com.jasonvillar.works.register.user_service.UserService;
import com.jasonvillar.works.register.user_service.UserServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final UserServiceRepository userServiceRepository;

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

    public Optional<Service> getOptionalByIdAndUserId(long id, long userId) {
        return this.serviceRepository.findOptionalByIdAndUserServiceListUserId(id, userId);
    }

    public List<Service> getListByUserId(long userId, int pageNumber, int rows) {
        Pageable page = PageRequest.of(pageNumber - 1, rows); //page start from 0
        return this.serviceRepository.findAllByUserServiceListUserId(userId, page);
    }

    public long getRowCountByUserId(long userId) {
        return this.serviceRepository.countByUserServiceListUserIdOrderByNameAsc(userId);
    }

    public List<Service> getListByNameLikeAndUserId(String name, long userId) {
        return this.serviceRepository.findAllByNameContainingIgnoreCaseAndUserServiceListUserId(name, userId);
    }

    public Service saveWithUser(Service entity, long userId) {
        Service serviceSaved = this.save(entity);

        UserService userService = UserService.builder()
                .serviceId(serviceSaved.getId())
                .userId(userId)
                .build();

        this.userServiceRepository.save(userService);

        return serviceSaved;
    }
}
