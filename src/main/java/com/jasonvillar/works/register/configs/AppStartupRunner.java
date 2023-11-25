package com.jasonvillar.works.register.configs;

import com.jasonvillar.works.register.Application;
import com.jasonvillar.works.register.user.User;
import com.jasonvillar.works.register.user.UserService;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = this.userService.getById(1);
        String value = user.getPassword();

        if (value.equals("admin")) {
            logger.info("... The Admin account has the default password ...");
            logger.info("... Searching for a new password ...");
            String change = environment.getProperty("admin_password");
            if (change == null) {
                logger.info("... Insert a new password with the env var \"ADMIN_PASSWORD\" ...");
                logger.info("... Shutdown system ...");
                Application.exitApplication();
            } else {
                logger.info("... The new password has been founded ...");
                String bcrypt = this.userService.plainPasswordToBcrypt(change);
                user.setPassword(bcrypt);
                this.userService.save(user);
                logger.info("... The admin account password has been changed ...");
            }
        }
    }
}
