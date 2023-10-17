package com.jasonvillar.works.register.user_client;

import com.jasonvillar.works.register.client.ClientService;
import com.jasonvillar.works.register.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserClientService {
    private final UserClientRepository repository;
    private final UserService userService;
    private final ClientService clientService;

    public List<UserClient> getList() {
        return this.repository.findAll();
    }

    public List<UserClient> getListByUserId(long userId) {
        return this.repository.findAllByUserId(userId);
    }

    public List<UserClient> getListByClientId(long clientId) {
        return this.repository.findAllByClientId(clientId);
    }

    public UserClient getById(long id) {
        return this.repository.findUserClientById(id);
    }

    public Optional<UserClient> getOptionalById(long id) {
        return this.repository.findOptionalById(id);
    }

    public Optional<UserClient> getOptionalByUserIdAndClientId(long userId, long clientId) {
        return this.repository.findOptionalByUserIdAndClientId(userId, clientId);
    }

    public boolean isExistUserIdAndClientId(long userId, long clientId) {
        return this.getOptionalByUserIdAndClientId(userId, clientId).isPresent();
    }

    public String getValidationsMessageWhenCantBeSaved(UserClient entity) {
        StringBuilder message = new StringBuilder();

        if (this.isExistUserIdAndClientId(entity.getUserId(), entity.getClientId())) {
            message.append("the combination of userId and serviceId must be unique");
        }

        if (!this.userService.isExistId(entity.getUserId())) {
            if (!message.isEmpty()) {
                message.append("\n");
            }
            message.append("userId must exist");
        }

        if (!this.clientService.isExistId(entity.getClientId())) {
            if (!message.isEmpty()) {
                message.append("\n");
            }
            message.append("clientId must exist");
        }

        return message.toString();
    }

    public UserClient save(UserClient userClient) {
        return this.repository.save(userClient);
    }
}
