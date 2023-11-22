package com.jasonvillar.works.register.user.user_not_validated.port.out;

import com.jasonvillar.works.register.user.user_not_validated.UserNotValidated;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserNotValidatedDTOAdapter implements Function<UserNotValidated, UserNotValidatedDTO> {
    @Override
    public UserNotValidatedDTO apply(UserNotValidated userNotValidated) {
        return new UserNotValidatedDTO(
                userNotValidated.getUserNotValidatedId().getName(),
                userNotValidated.getUserNotValidatedId().getEmail()
        );
    }
}
