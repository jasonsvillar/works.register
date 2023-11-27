package com.jasonvillar.works.register.user.user_not_validated;

import com.jasonvillar.works.register.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserNotValidatedService {

    private final UserNotValidatedRepository userNotValidatedRepository;

    @Value("${backend.public-url}")
    private String backendPublicUrl;

    private final EmailService emailService;

    private SecureRandom rand = new SecureRandom();

    Logger logger = LoggerFactory.getLogger(UserNotValidatedService.class);

    public String makeRandomValidationCode() {
        int upperbound = 10;

        StringBuilder code = new StringBuilder();

        for (int a = 0; a < 6; a++) {
            int number = rand.nextInt(upperbound);
            code.append(number);
        }

        return code.toString();
    }

    public boolean sendValidationCode(String name, String email, String code, String frontEndValidationUrl, boolean isUser) {
        String urlValidateAccount = backendPublicUrl;

        if (isUser) {
            urlValidateAccount = urlValidateAccount
                    + "/api/v1/user";
        } else {
            urlValidateAccount = urlValidateAccount
                    + "/api/v1/pre-user";
        }

        urlValidateAccount = urlValidateAccount
                + "/validate/name/" + name
                + "/email/" + email
                + "/code/" + code;

        String href = "<a href='" + urlValidateAccount + "'>here.</a>";

        if (frontEndValidationUrl == null) {
            frontEndValidationUrl = "";
        }

        if (!frontEndValidationUrl.isEmpty() && !frontEndValidationUrl.isBlank()) {
            if (!frontEndValidationUrl.endsWith("/")) {
                frontEndValidationUrl = frontEndValidationUrl.concat("/");
            }

            urlValidateAccount = frontEndValidationUrl + code;
            href = "<a href='" + frontEndValidationUrl + code + "'>here.</a>";
        }

        logger.info("... Sending email for validate your account ...");

        boolean send = this.emailService.sendSimpleMessage(
                email,
                "Validate Works Api account",
                "Your confirmation code is " + code + "."
                        + "<br>Please validate your account " + href
                        + "<br><br>" + urlValidateAccount
        );

        logger.info("... The email has been send ...");
        logger.info("... Please validate your email account ...");

        return send;
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
