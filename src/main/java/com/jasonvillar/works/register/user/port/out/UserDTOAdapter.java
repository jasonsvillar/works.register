package com.jasonvillar.works.register.user.port.out;

import com.jasonvillar.works.register.user.port.in.UserRequest;
import com.jasonvillar.works.register.user.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserDTOAdapter implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
