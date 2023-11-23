package com.jasonvillar.works.register.user_service;

import com.jasonvillar.works.register.service.ServiceService;
import com.jasonvillar.works.register.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public List<UserService> saveAll(List<UserService> userServiceList) {
        return this.userServiceRepository.saveAll(userServiceList);
    }

    @Transactional
    public boolean deleteByServiceIdAndUserId(long serviceId, long userId) {
        long deletedRows = this.userServiceRepository.deleteByServiceIdAndUserId(serviceId, userId);
        return deletedRows > 0;
    }

    @Transactional
    public List<UserService> deleteByServicesIdAndUserId(List<Long> serviceIdList, long userId) {
        return this.userServiceRepository.deleteByServiceIdInAndUserId(serviceIdList, userId);
    }

    public UserService setServiceEntityIntoUserService(UserService userService) {
        userService.setService(this.serviceService.getById(userService.getServiceId()));
        return userService;
    }

    public UserService setUserEntityIntoUserService(UserService userService) {
        userService.setUser(this.userService.getById(userService.getUserId()));
        return userService;
    }

    public List<UserService> makeUserServicesFromServiceIdListAndUser(List<Long> serviceIdLongList, User user) {
        List<UserService> userServiceListToSave = new ArrayList<>();

        serviceIdLongList.forEach(
                serviceId -> {
                    UserService serviceOfUser = UserService.builder()
                            .serviceId(serviceId)
                            .userId(user.getId())
                            .build();

                    serviceOfUser = setServiceEntityIntoUserService(serviceOfUser);
                    serviceOfUser.setUser(user);

                    userServiceListToSave.add(serviceOfUser);
                }
        );

        return userServiceListToSave;
    }
}
