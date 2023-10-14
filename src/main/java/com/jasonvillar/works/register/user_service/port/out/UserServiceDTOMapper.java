package com.jasonvillar.works.register.user_service.port.out;

import com.jasonvillar.works.register.service.port.out.ServiceDTOMapper;
import com.jasonvillar.works.register.user.port.out.UserDTOMapper;
import com.jasonvillar.works.register.user_service.UserService;
import com.jasonvillar.works.register.user_service.port.in.UserServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserServiceDTOMapper implements Function<UserService, UserServiceDTO> {

    private final UserDTOMapper userDTOMapper;

    private final ServiceDTOMapper serviceDTOMapper;

    @Override
    public UserServiceDTO apply(UserService userService) {
        return new UserServiceDTO(userService.getId(), userDTOMapper.apply(userService.getUser()), serviceDTOMapper.apply(userService.getService()));
    }

    public UserService toEntity(UserServiceRequest userServiceRequest) {
        return new UserService(userServiceRequest.userId(), userServiceRequest.serviceId());
    }
}
