package com.jasonvillar.works.register.user.port.in;

import com.jasonvillar.works.register.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserRequestAdapter {
    public User toEntity(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.name())
                .email(userRequest.email())
                .password(userRequest.password())
                .build();
    }
}
