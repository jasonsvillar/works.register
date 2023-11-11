package com.jasonvillar.works.register.user_service.port.out;

import com.jasonvillar.works.register.service.port.out.ServiceDTOAdapter;
import com.jasonvillar.works.register.user.port.out.UserDTOAdapter;
import com.jasonvillar.works.register.user_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserServiceDTOAdapter implements Function<UserService, UserServiceDTO> {

    private final UserDTOAdapter userDTOAdapter;
    private final ServiceDTOAdapter serviceDTOAdapter;

    @Override
    public UserServiceDTO apply(UserService userService) {
        return new UserServiceDTO(
                userService.getId(),
                userDTOAdapter.apply(userService.getUser()),
                serviceDTOAdapter.apply(userService.getService())
        );
    }
}
