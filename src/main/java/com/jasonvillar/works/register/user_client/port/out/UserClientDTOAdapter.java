package com.jasonvillar.works.register.user_client.port.out;

import com.jasonvillar.works.register.client.port.out.ClientDTOAdapter;
import com.jasonvillar.works.register.user.port.out.UserDTOAdapter;
import com.jasonvillar.works.register.user_client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserClientDTOAdapter implements Function<UserClient, UserClientDTO> {

    private final UserDTOAdapter userDTOAdapter;
    private final ClientDTOAdapter clientDTOAdapter;

    @Override
    public UserClientDTO apply(UserClient userClient) {
        return new UserClientDTO(userClient.getId(), userDTOAdapter.apply(userClient.getUser()), clientDTOAdapter.apply(userClient.getClient()));
    }
}
