package com.jasonvillar.works.register.user_client.port.in;

import com.jasonvillar.works.register.user_client.UserClient;
import org.springframework.stereotype.Component;

@Component
public class UserClientRequestAdapter {
    public UserClient toEntity(UserClientRequest userClientRequest) {
        return new UserClient(userClientRequest.userId(), userClientRequest.clientId());
    }
}
