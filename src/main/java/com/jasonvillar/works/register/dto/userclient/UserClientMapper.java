package com.jasonvillar.works.register.dto.userclient;

import com.jasonvillar.works.register.dto.client.ClientMapper;
import com.jasonvillar.works.register.dto.user.UserMapper;
import com.jasonvillar.works.register.entities.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserClientMapper implements Function<UserClient, UserClientDTO> {

    private final UserMapper userMapper;
    private final ClientMapper clientMapper;

    @Override
    public UserClientDTO apply(UserClient userClient) {
        return new UserClientDTO(userClient.getId(), userMapper.apply(userClient.getUser()), clientMapper.apply(userClient.getClient()));
    }

    public UserClient toEntity(UserClientRequest userClientRequest) {
        return new UserClient(userClientRequest.userId(), userClientRequest.clientId());
    }
}
