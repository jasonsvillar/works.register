package com.jasonvillar.works.register.user.user_not_validated;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserNotValidatedService {

    private final UserNotValidatedRepository userNotValidatedRepository;

    private SecureRandom rand = new SecureRandom();

    public String makeRandomValidationCode() {
        int upperbound = 10;

        StringBuilder code = new StringBuilder();

        for (int a = 0; a < 6; a++) {
            int number = rand.nextInt(upperbound);
            code.append(number);
        }

        return code.toString();
    }

    public UserNotValidated makeValidationCodeForUserNotValidated(UserNotValidated userNotValidated) {
        String code = this.makeRandomValidationCode();
        userNotValidated.setCode(code);
        return userNotValidated;
    }

    public UserNotValidated save(UserNotValidated userNotValidated) {
        return userNotValidatedRepository.save(userNotValidated);
    }

    public Optional<UserNotValidated> findOptionalById(UserNotValidatedId userNotValidatedId) {
        return userNotValidatedRepository.findById(userNotValidatedId);
    }

    public Optional<UserNotValidated> findOptionalByIdAndCode(UserNotValidatedId userNotValidatedId, String code) {
        return userNotValidatedRepository.findByUserNotValidatedIdAndCode(userNotValidatedId, code);
    }

    @Transactional
    public boolean deleteById(UserNotValidatedId userNotValidatedId) {
        return userNotValidatedRepository.deleteByUserNotValidatedId(userNotValidatedId) != 0L;
    }
}
