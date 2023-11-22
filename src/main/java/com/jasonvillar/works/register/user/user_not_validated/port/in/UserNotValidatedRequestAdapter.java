package com.jasonvillar.works.register.user.user_not_validated.port.in;

import com.jasonvillar.works.register.user.user_not_validated.UserNotValidated;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedId;
import org.springframework.stereotype.Component;

@Component
public class UserNotValidatedRequestAdapter {
    public UserNotValidated toEntity(UserNotValidatedRequest userNotValidatedRequest) {
        return UserNotValidated.builder()
                .userNotValidatedId(
                        UserNotValidatedId.builder()
                        .name( userNotValidatedRequest.name() )
                        .email( userNotValidatedRequest.email() )
                        .build()
                )
                .password(userNotValidatedRequest.password())
                .build();
    }
}
