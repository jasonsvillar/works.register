package com.jasonvillar.works.register.user_service;

import com.jasonvillar.works.register.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceService {
    private final UserServiceRepository userServiceRepository;

    private final com.jasonvillar.works.register.user.UserService userService;

    private final ServiceService serviceService;

    public UserService getById(long id) {
        return this.userServiceRepository.findUserServiceById(id);
    }

    public Optional<UserService> getOptionalById(long id) {
        return this.userServiceRepository.findOptionalById(id);
    }

    public List<UserService> getList() {
        return this.userServiceRepository.findAll();
    }

    public List<UserService> getListByUserId(long userId) {
        return this.userServiceRepository.findAllByUserId(userId);
    }

    public List<UserService> getListByServiceId(long serviceId) {
        return this.userServiceRepository.findAllByServiceId(serviceId);
    }

    public Optional<UserService> getOptionalByUserIdAndServiceId(long userId, long serviceId) {
        return this.userServiceRepository.findOptionalByUserIdAndServiceId(userId, serviceId);
    }

    public boolean isExistUserIdAndServiceId(long userId, long serviceId) {
        return this.getOptionalByUserIdAndServiceId(userId, serviceId).isPresent();
    }

    public String getValidationsMessageWhenCantBeSaved(UserService entity) {
        StringBuilder message = new StringBuilder();

        if (this.isExistUserIdAndServiceId(entity.getUserId(), entity.getServiceId())) {
            message.append("the combination of userId and serviceId must be unique");
        }

        if (!this.userService.isExistId(entity.getUserId())) {
            if (!message.isEmpty()) {
                message.append(", ");
            }
            message.append("userId must exist");
        }

        if (!this.serviceService.isExistId(entity.getServiceId())) {
            if (!message.isEmpty()) {
                message.append(", ");
            }
            message.append("serviceId must exist");
        }

        return message.toString();
    }

    public UserService save(UserService userService) {
        return this.userServiceRepository.save(userService);
    }

    @Transactional
    public boolean deleteByServiceIdAndUserId(long serviceId, long userId) {
        long deletedRows = this.userServiceRepository.deleteByServiceIdAndUserId(serviceId, userId);
        if (deletedRows > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public List<UserService> deleteByServicesIdAndUserId(List<Long> serviceIdList, long userId) {
        return this.userServiceRepository.deleteByServiceIdInAndUserId(serviceIdList, userId);
    }
}
