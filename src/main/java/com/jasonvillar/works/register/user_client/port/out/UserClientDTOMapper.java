package com.jasonvillar.works.register.user_client.port.out;

import com.jasonvillar.works.register.client.port.out.ClientDTOMapper;
import com.jasonvillar.works.register.user_client.port.in.UserClientRequest;
import com.jasonvillar.works.register.user.port.out.UserDTOMapper;
import com.jasonvillar.works.register.user_client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserClientDTOMapper implements Function<UserClient, UserClientDTO> {

    private final UserDTOMapper userDTOMapper;
    private final ClientDTOMapper clientDTOMapper;

    @Override
    public UserClientDTO apply(UserClient userClient) {
        return new UserClientDTO(userClient.getId(), userDTOMapper.apply(userClient.getUser()), clientDTOMapper.apply(userClient.getClient()));
    }

    public UserClient toEntity(UserClientRequest userClientRequest) {
        return new UserClient(userClientRequest.userId(), userClientRequest.clientId());
    }
}
