package com.jasonvillar.works.register.validations;

import com.jasonvillar.works.register.entities.UserService;
import com.jasonvillar.works.register.repositories.UserServiceRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UniqueUserServiceValidator implements ConstraintValidator<UniqueUserService, UserService> {
    private final UserServiceRepository userServiceRepository;

    @Override
    public boolean isValid(UserService userService, ConstraintValidatorContext context) {
        if (userService.getUserId() == null || userService.getServiceId() == null) {
            return true;
        }

        Optional<UserService> userOptional = this.userServiceRepository.findOptionalByUserIdAndServiceId(userService.getUserId(), userService.getServiceId());

        return userOptional.isEmpty();
    }
}
