package com.jasonvillar.works.register.dto.userservice;

import com.jasonvillar.works.register.dto.service.ServiceMapper;
import com.jasonvillar.works.register.dto.user.UserMapper;
import com.jasonvillar.works.register.entities.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserServiceMapper implements Function<UserService, UserServiceDTO> {

    private final UserMapper userMapper;

    private final ServiceMapper serviceMapper;

    @Override
    public UserServiceDTO apply(UserService userService) {
        return new UserServiceDTO(userService.getId(), userMapper.apply(userService.getUser()), serviceMapper.apply(userService.getService()));
    }

    public UserService toEntity(UserServiceRequest userServiceRequest) {
        return new UserService(userServiceRequest.userId(), userServiceRequest.serviceId());
    }
}
