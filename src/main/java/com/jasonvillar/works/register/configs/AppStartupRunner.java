package com.jasonvillar.works.register.configs;

import com.jasonvillar.works.register.Application;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserService;
import com.jasonvillar.works.register.user.user_not_validated.UserNotValidatedService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppStartupRunner implements ApplicationRunner {
    Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);

    private final Environment environment;

    private final UserService userService;

    private final UserNotValidatedService userNotValidatedService;

    private static final String SEPARATOR = "......";

    private static final String EMPTY_EMAIL = "empty_email";

    private boolean test = false;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = this.userService.getById(1);
        String value = user.getPassword();

        boolean mustShutdown = false;

        if (value.equals("admin")) {
            logger.info(SEPARATOR);
            logger.info("... The Admin account has not password ...");
            logger.info("... Searching for a new password ...");
            String change = environment.getProperty("admin_password");
            if (change == null) {
                logger.info("... Insert a new password with the env var \"ADMIN_PASSWORD\" ...");
                mustShutdown = true;
            } else {
                logger.info("... The new password has been founded ...");
                String bcrypt = this.userService.plainPasswordToBcrypt(change);
                user.setPassword(bcrypt);
                this.userService.save(user);
                logger.info("... The admin account password has been changed ...");
            }
        }

        String email = user.getEmail();
        String newEmail = environment.getProperty("admin_email");

        if (email.equals(EMPTY_EMAIL) && newEmail == null) {
            logger.info(SEPARATOR);
            logger.info("... The Admin account has not email ...");
            logger.info("... Insert a new email with the env var \"ADMIN_EMAIL\" ...");
            mustShutdown = true;
        }

        if ( (email.equals(EMPTY_EMAIL) || !user.isValidated()) && newEmail != null ) {
            logger.info("... The new email has been founded ...");
            user = this.userService.updateEmailAndGenerateCodeToUser(user, newEmail);
            this.userNotValidatedService.sendValidationCode(user.getName(), user.getEmail(), user.getCode(), null, true);
        }

        if (!email.equals(EMPTY_EMAIL) && !user.isValidated() && newEmail == null) {
            user = this.userService.generateCodeToUser(user);
            this.userNotValidatedService.sendValidationCode(user.getName(), user.getEmail(), user.getCode(), null, true);
        }

        if (this.environment.getProperty("spring.mail.username") == null) {
            logger.info("... The system email account property spring.mail.username has not been set ...");
            mustShutdown = true;
        }

        if (mustShutdown) {
            logger.info(SEPARATOR);
            logger.info("... Shutdown system ...");
            if (!test) {
                Application.exitApplication();
            }
        }
    }
}
